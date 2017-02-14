# Presenters that survive `Activity` configuration changes using Dagger2

Recently I was looking for an example how to make my `Presenters` survive an `Activity` recreation when a configuration change occurs. All examples that I found were using `Fragments`, looked way too complicated or didn't use `Dagger`. So I decided come up with my own solution and setup this project, so it may help other people.

## The problem
My project structure was built in a way, that a `Presenter` was coupled to it's view lifecycle. I did directly use `Activities`, so the `Presenter` was destroyed once the `Activity` was destroyed. That was fine for my project. Then I got to a point where I needed to run a long network operation. 

On a button click the `Presenter` subscribed to an `Observable` that would perform the network operation. The `Presenter` updated the `View` with the progress. So far it worked. But on a screen rotation the `Activity` was recreated and therefore the `Presenter`, too. The new instance knew nothing about the progress.

So I wanted to change my `Presenters`, so they survive the `Activity` recreation, keep subscribed to the `Observable`, that performs the network task, and update the `View` once it is back.

## The idea
[Google officially suggests](https://developer.android.com/guide/topics/resources/runtime-changes.html) to use `Fragment.setRetainInstance(true)`, but I felt like adding a `Fragment` only for this purpose was too much. I also read about keeping `Presenters` in a static `Map`, but couldn't find an example, also nothing that showcases this with `Dagger2`. My `Presenters` currently were injected anew every time an `Activity` was created, with this `Map` this should only happen when an `Activity` is really created for the first time, not on a recreation.

> **_Note:_** The following section requires you to be familar with `Dagger2` and `MVP`

## The solution
First I needed to create some cache, that can be injected everywhere I needed it. I created a new class for that:
```java
public final class PresenterCache {

    private final Map<String, BasePresenter> cachedPresenters = new ArrayMap<>();

    (...)
}
```
As a key I decided to simply use class names, you can use whatever you want.

I added this to my `ApplicationModule`, so I could get the singleton `PresenterCache` everywhere where it's needed
```java
@Provides
@Singleton
PresenterCache providePresenterCache() {
  return new PresenterCache();
}
```

Now I could inject the `PresenterCache` in my `BaseActivity` class. I added the following method, which is called in `onCreate()` and checks if there is a cached presenter. If not, it creates a new one using `Dagger`.
```java
private void restoreOrCreatePresenter() {
  // try to get a cached presenter
  presenter = presenterCache.getPresenter(getClass().getName());
    if (presenter == null) {
       // no cached one found, create a new one
       presenter = presenterComponent.getPresenter();
       presenterCache.putPresenter(getClass().getName(), presenter);
    }
  presenter.bindView(this);
}
```

So what's that `presenterComponent` that creates a new presenter for me? It's simply an `Interface` that every `Dagger Component` extends, that needs to provide a `Presenter`:
```java
public interface PresenterComponent<T extends BasePresenter> {

    T getPresenter();
}
```

When `Dagger` builds the `Components` it will include a method `getPresenter()` now which returns you a new instance of type `T`.

> So far, if an `Activity` gets recreated, it will use the cached `Presenter` and simply bind itself to this one, allowing the `Presenter` to update the view accordingly and, more important, continue e.g. network operations.

Bad side: Currently the `Presenter` will also be cached if the `Activity` is closed normally, e.g. by a back press.
So let's modify `BaseActivity.onStop()`:
```java
@Override
protected void onStop() {
  if (!isChangingConfigurations()) {
    // activity is stopped normally, remove the cached presenter so it's not cached
    // even if activity gets killed
    presenterCache.removePresenter(presenter);
  }
  // onStop will clear view reference
  presenter.onStop(isChangingConfigurations());
}
```
From the documentation of `isChangingConfigurations()`
> Check to see whether this activity is in the process of being destroyed in order to be recreated with a new configuration.

Awesome, so if we know our `Activity` is being stopped normally we can remove it from `PresenterCache`. Additionally we clear the view reference in the `Presenter`. We do this in `onStop()` as this is the last method Android guarantees us to be called. Afterwards the `Activity` may be killed at any given time.

There is one last modification needed. If the activity is simply stopped, e.g. because we launched a new `Activity` that made our `Activity` invisible, and now restarts, because that `Activity` has been closed, we want to put the presenter back to the cache and bind ourself to it:
```java
@Override
protected void onRestart() {
  super.onRestart();
  // put presenter back to cache and re-bind view
  presenterCache.putPresenter(getClass().getName(), presenter);
  presenter.bindView(this);
  presenter.onRestart();
}
```
  
Check out the sample for a full example.

![Example](example.gif)
