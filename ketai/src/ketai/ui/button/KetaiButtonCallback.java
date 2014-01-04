package ketai.ui.button;

import android.widget.Button;
import processing.core.PApplet;

public interface KetaiButtonCallback {
    public void onClick(KetaiButton button);
}

class KetaiButtonCallBackImpl implements KetaiButtonCallback {

    @Override
    public void onClick(KetaiButton button) {
        PApplet.println("Default Button Callback");
    }
}