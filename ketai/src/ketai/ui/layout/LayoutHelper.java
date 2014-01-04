package ketai.ui.layout;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.*;

public class LayoutHelper {
    public static ViewGroup.LayoutParams createLayoutParamsFor(String layoutType) {
        if (layoutType.equals("RelativeLayout")) {
            return new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        } else if (layoutType.equals("LinearLayout")) {
            return new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        } else if (layoutType.equals("GridLayout")) {
            return new GridLayout.LayoutParams(GridLayout.spec(0), GridLayout.spec(0));
        } else if (layoutType.equals("ScrollView")) {
            return new ScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        } else if (layoutType.equals("HorizontalScrollView")) {
            return new HorizontalScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        } else {
            throw new IllegalArgumentException("Unknown Layout Type");
        }
    }

    public static ViewGroup createViewGroup(String layoutType, Context parentContext) {
        if (layoutType.equals("RelativeLayout")) {
            return new RelativeLayout(parentContext);
        } else if (layoutType.equals("LinearLayout")) {
            return new LinearLayout(parentContext);
        } else if (layoutType.equals("GridLayout")) {
            return new GridLayout(parentContext);
        } else if (layoutType.equals("ScrollView")) {
            return new ScrollView(parentContext);
        } else if (layoutType.equals("HorizontalScrollView")) {
            return new HorizontalScrollView(parentContext);
        } else {
            throw new IllegalArgumentException("Unknown Layout Type");
        }
    }

}
