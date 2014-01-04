package ketai.ui.layout;

import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import processing.core.PApplet;

public class KetaiLayout {
    private PApplet parent;
    protected ViewGroup baseLayout;

    /*
        If `this` is not the root of the hierarchy, we add the layout to this ViewGroup.
     */
    private ViewGroup parentLayout;

    /*
        Flag denoting if this layout instance is in the root of our layout hierarchy.
     */
    private boolean isRootElement = false;

    public KetaiLayout(PApplet _parent, String _viewType) {
        parent = _parent;
        isRootElement = true;
        addToUI(_viewType);
    }

    public KetaiLayout(KetaiLayout _parentLayout, String _viewType) {
        parentLayout = _parentLayout.getLayout();
        parent = _parentLayout.getParent();
        addToUI(_viewType);
    }

    public PApplet getParent() {
        return parent;
    }

    public ViewGroup getLayout() {
        return baseLayout;
    }

    public void addView(final View childView) {
        parent.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                baseLayout.addView(childView);
            }
        });
    }

    public void changeLinearLayoutToVertical() {
        if (baseLayout instanceof LinearLayout) {
            ((LinearLayout) baseLayout).setOrientation(LinearLayout.VERTICAL);
        } else {
            throw new IllegalStateException("Layout is not LinearLayout");
        }
    }

    public void changeLinearLayoutToHorizontal() {
        if (baseLayout instanceof LinearLayout) {
            ((LinearLayout) baseLayout).setOrientation(LinearLayout.HORIZONTAL);
        } else {
            throw new IllegalStateException("Layout is not LinearLayout");
        }
    }

    private void addToUI(String _viewType) {
        baseLayout = LayoutHelper.createViewGroup(_viewType, parent.getApplicationContext());
        final ViewGroup.LayoutParams layoutParams = LayoutHelper.createLayoutParamsFor(_viewType);

        this.parent.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isRootElement) {
                    parent.addContentView(baseLayout, layoutParams);
                } else {
                    parentLayout.addView(baseLayout, layoutParams);
                }
            }
        });
    }


}
