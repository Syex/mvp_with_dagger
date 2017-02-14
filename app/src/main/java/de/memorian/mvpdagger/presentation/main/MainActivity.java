package de.memorian.mvpdagger.presentation.main;

import android.widget.TextView;

import de.memorian.mvpdagger.R;
import de.memorian.mvpdagger.presentation.base.BaseActivity;

import butterknife.BindView;

public class MainActivity extends BaseActivity<MainPresenter> implements MainView {

    @BindView(R.id.tv_example)
    TextView exampleTextView;

    @Override
    protected int getLayoutFile() {
        return R.layout.activity_main;
    }

    @Override
    protected void inject() {
        presenterComponent = DaggerMainComponent.builder()
                .applicationComponent(getApplicationComponent())
                .build();
        ((MainComponent) presenterComponent).inject(this);
    }

    @Override
    public void updateExampleValue(String newValue) {
        exampleTextView.setText(newValue);
    }

    @Override
    public void initLayout() {
        exampleTextView.setText(presenter.getExampleValue());
    }
}
