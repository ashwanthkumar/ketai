package ketai.ui.button;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import ketai.ui.layout.KetaiLayout;

/*
Usage:
    KetaiLayout layout = new KetaiLayout(this, "GridLayout");

    KetaiButton button = new KetaiButton(layout, "Sample Button #1"); // Does nothing

    class CloseButtonImpl implements KetaiButtonCallback {
        void onClick(Button button) {
            exit();
        }
    }
    KetaiButton button = new KetaiButton(layout, "Close", new CloseButtonImpl()); // Exits the application

 */
public class KetaiButton {
    private KetaiLayout parentView;
    private Button button;

    public KetaiButton(KetaiLayout _parentLayout, String buttonText) {
        parentView = _parentLayout;
        button = new Button(parentView.getParent().getApplicationContext());
        initButton(buttonText, new KetaiButtonCallBackImpl());
    }

    public KetaiButton(KetaiLayout _parentLayout, final String buttonText, KetaiButtonCallback buttonCallback) {
        parentView = _parentLayout;
        button = new Button(_parentLayout.getParent().getApplicationContext());
        initButton(buttonText, buttonCallback);
    }

    private void initButton(final String text, final KetaiButtonCallback callbackImpl) {
        final KetaiButton myself = this;
        parentView.getParent().runOnUiThread(new Runnable() {
            public void run() {
                button.setText(text);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callbackImpl.onClick(myself);
                    }
                });
                parentView.getLayout().addView(button);
            }
        });
    }

    public void setTitle(final String text) {
        parentView.getParent().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                button.setText(text);
            }
        });
    }

    public String getTitle() {
        return button.getText().toString();
    }

    public void setWidth(final int widthInPixels) {
        parentView.getParent().runOnUiThread(new Runnable() {
            public void run() {
                button.setWidth(widthInPixels);
            }
        });
    }

    public void setHeight(final int heightInPixels) {
        parentView.getParent().runOnUiThread(new Runnable() {
            public void run() {
                button.setHeight(heightInPixels);
            }
        });
    }

    public void wrapContentOnHeight() {
        parentView.getParent().runOnUiThread(new Runnable() {
            public void run() {
                ViewGroup.LayoutParams layoutParams = button.getLayoutParams();
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                button.setLayoutParams(layoutParams);
            }
        });
    }

    public void wrapContentOnWidth() {
        parentView.getParent().runOnUiThread(new Runnable() {
            public void run() {
                ViewGroup.LayoutParams layoutParams = button.getLayoutParams();
                layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                button.setLayoutParams(layoutParams);
            }
        });
    }
}
