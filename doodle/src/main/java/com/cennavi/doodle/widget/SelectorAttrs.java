package com.cennavi.doodle.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import com.cennavi.doodle.R.styleable;
public class SelectorAttrs {
    public static final int RECTANGLE = 0;
    public static final int OVAL = 1;
    public static final int LINE = 2;
    public static final int RING = 3;

    public SelectorAttrs() {
    }

    public static void obtainsAttrs(Context context, View view, AttributeSet attrs) {
        Drawable bitmapDrawable = view.getBackground();
        if (!(bitmapDrawable instanceof StateListDrawable)) {
            if (Build.VERSION.SDK_INT < 21 || !(bitmapDrawable instanceof RippleDrawable)) {
                TypedArray a = context.obtainStyledAttributes(attrs, styleable.SelectorAttrs);
                if (bitmapDrawable instanceof ColorDrawable) {
                    bitmapDrawable = null;
                }

                GradientDrawable colorShapeDrawable = null;
                GradientDrawable colorShapeDrawablePressed = null;
                GradientDrawable colorShapeDrawableSelected = null;
                GradientDrawable colorShapeDrawableDisable = null;
                Drawable bitmapDrawablePressed = null;
                Drawable bitmapDrawableSelected = null;
                Drawable bitmapDrawableDisable = null;
                if (a.hasValue(styleable.SelectorAttrs_sel_background_pressed) || a.hasValue(styleable.SelectorAttrs_sel_background_border_pressed)) {
                    bitmapDrawablePressed = a.getDrawable(styleable.SelectorAttrs_sel_background_pressed);
                    if (bitmapDrawablePressed instanceof ColorDrawable) {
                        bitmapDrawablePressed = null;
                    }

                    if (bitmapDrawablePressed == null) {
                        colorShapeDrawablePressed = new GradientDrawable();
                    }
                }

                if (a.hasValue(styleable.SelectorAttrs_sel_background_selected) || a.hasValue(styleable.SelectorAttrs_sel_background_border_selected)) {
                    bitmapDrawableSelected = a.getDrawable(styleable.SelectorAttrs_sel_background_selected);
                    if (bitmapDrawableSelected instanceof ColorDrawable) {
                        bitmapDrawableSelected = null;
                    }

                    if (bitmapDrawableSelected == null) {
                        colorShapeDrawableSelected = new GradientDrawable();
                    }
                }

                if (a.hasValue(styleable.SelectorAttrs_sel_background_disable) || a.hasValue(styleable.SelectorAttrs_sel_background_border_disable)) {
                    bitmapDrawableDisable = a.getDrawable(styleable.SelectorAttrs_sel_background_disable);
                    if (bitmapDrawableDisable instanceof ColorDrawable) {
                        bitmapDrawableDisable = null;
                    }

                    if (bitmapDrawableDisable == null) {
                        colorShapeDrawableDisable = new GradientDrawable();
                    }
                }

                int background = 0;
                if (bitmapDrawable == null) {
                    Drawable bg = view.getBackground();
                    if (bg == null && a.hasValue(styleable.SelectorAttrs_sel_background)) {
                        bg = a.getDrawable(styleable.SelectorAttrs_sel_background);
                    }

                    if (bg instanceof ColorDrawable) {
                        colorShapeDrawable = new GradientDrawable();
                        background = ((ColorDrawable)bg).getColor();
                        colorShapeDrawable.setColor(background);
                    }
                }

                if (colorShapeDrawablePressed != null) {
                    colorShapeDrawablePressed.setColor(background);
                }

                if (colorShapeDrawableSelected != null) {
                    colorShapeDrawableSelected.setColor(background);
                }

                if (colorShapeDrawableDisable != null) {
                    colorShapeDrawableDisable.setColor(background);
                }

                int shape = 0;
                if (a.hasValue(styleable.SelectorAttrs_sel_background_shape)) {
                    shape = a.getInt(styleable.SelectorAttrs_sel_background_shape, 0);
                    if (colorShapeDrawable != null) {
                        colorShapeDrawable.setShape(shape);
                    }

                    if (colorShapeDrawablePressed != null) {
                        colorShapeDrawablePressed.setShape(shape);
                    }

                    if (colorShapeDrawableSelected != null) {
                        colorShapeDrawableSelected.setShape(shape);
                    }

                    if (colorShapeDrawableDisable != null) {
                        colorShapeDrawableDisable.setShape(shape);
                    }
                }

                int radius = 0;
                float[] cornerRadii = null;
                int backgroundCorners;
                if (a.hasValue(styleable.SelectorAttrs_sel_background_corners)) {
                    backgroundCorners = a.getDimensionPixelOffset(styleable.SelectorAttrs_sel_background_corners, 0);
                    radius = backgroundCorners;
                    if (colorShapeDrawable != null) {
                        colorShapeDrawable.setCornerRadius((float)backgroundCorners);
                    }

                    if (colorShapeDrawablePressed != null) {
                        colorShapeDrawablePressed.setCornerRadius((float)backgroundCorners);
                    }

                    if (colorShapeDrawableSelected != null) {
                        colorShapeDrawableSelected.setCornerRadius((float)backgroundCorners);
                    }

                    if (colorShapeDrawableDisable != null) {
                        colorShapeDrawableDisable.setCornerRadius((float)backgroundCorners);
                    }

                    cornerRadii = new float[]{(float)backgroundCorners, (float)backgroundCorners, (float)backgroundCorners, (float)backgroundCorners, (float)backgroundCorners, (float)backgroundCorners, (float)backgroundCorners, (float)backgroundCorners};
                }

                backgroundCorners = a.getDimensionPixelSize(styleable.SelectorAttrs_sel_background_corner_topLeft, radius);
                int topRightRadius = a.getDimensionPixelSize(styleable.SelectorAttrs_sel_background_corner_topRight, radius);
                int bottomLeftRadius = a.getDimensionPixelSize(styleable.SelectorAttrs_sel_background_corner_bottomLeft, radius);
                int bottomRightRadius = a.getDimensionPixelSize(styleable.SelectorAttrs_sel_background_corner_bottomRight, radius);
                if (backgroundCorners != radius || topRightRadius != radius || bottomLeftRadius != radius || bottomRightRadius != radius) {
                    cornerRadii = new float[]{(float)backgroundCorners, (float)backgroundCorners, (float)topRightRadius, (float)topRightRadius, (float)bottomRightRadius, (float)bottomRightRadius, (float)bottomLeftRadius, (float)bottomLeftRadius};
                    if (colorShapeDrawable != null) {
                        colorShapeDrawable.setCornerRadii(cornerRadii);
                    }

                    if (colorShapeDrawablePressed != null) {
                        colorShapeDrawablePressed.setCornerRadii(cornerRadii);
                    }

                    if (colorShapeDrawableSelected != null) {
                        colorShapeDrawableSelected.setCornerRadii(cornerRadii);
                    }

                    if (colorShapeDrawableDisable != null) {
                        colorShapeDrawableDisable.setCornerRadii(cornerRadii);
                    }
                }

                int backgroundBorderWidth = a.getDimensionPixelOffset(styleable.SelectorAttrs_sel_background_border_width, -1);
                if (backgroundBorderWidth != -1) {
                    if (colorShapeDrawable != null) {
                        colorShapeDrawable.setStroke(backgroundBorderWidth, 0);
                    }

                    if (colorShapeDrawablePressed != null) {
                        colorShapeDrawablePressed.setStroke(backgroundBorderWidth, 0);
                    }

                    if (colorShapeDrawableSelected != null) {
                        colorShapeDrawableSelected.setStroke(backgroundBorderWidth, 0);
                    }

                    if (colorShapeDrawableDisable != null) {
                        colorShapeDrawableDisable.setStroke(backgroundBorderWidth, 0);
                    }
                }

                int backgroundBorderDisable;
                if (a.hasValue(styleable.SelectorAttrs_sel_background_border_color)) {
                    backgroundBorderDisable = a.getColor(styleable.SelectorAttrs_sel_background_border_color, -1);
                    if (colorShapeDrawable != null) {
                        colorShapeDrawable.setStroke(backgroundBorderWidth, backgroundBorderDisable);
                    }

                    if (colorShapeDrawablePressed != null) {
                        colorShapeDrawablePressed.setStroke(backgroundBorderWidth, backgroundBorderDisable);
                    }

                    if (colorShapeDrawableSelected != null) {
                        colorShapeDrawableSelected.setStroke(backgroundBorderWidth, backgroundBorderDisable);
                    }

                    if (colorShapeDrawableDisable != null) {
                        colorShapeDrawableDisable.setStroke(backgroundBorderWidth, backgroundBorderDisable);
                    }
                }

                if (colorShapeDrawable != null) {
                    colorShapeDrawable.setColor(background);
                }

                if (colorShapeDrawablePressed != null && a.hasValue(styleable.SelectorAttrs_sel_background_pressed)) {
                    backgroundBorderDisable = a.getColor(styleable.SelectorAttrs_sel_background_pressed, 0);
                    colorShapeDrawablePressed.setColor(backgroundBorderDisable);
                }

                if (colorShapeDrawablePressed != null && a.hasValue(styleable.SelectorAttrs_sel_background_border_pressed)) {
                    backgroundBorderDisable = a.getColor(styleable.SelectorAttrs_sel_background_border_pressed, -1);
                    colorShapeDrawablePressed.setStroke(backgroundBorderWidth, backgroundBorderDisable);
                }

                if (colorShapeDrawableSelected != null && a.hasValue(styleable.SelectorAttrs_sel_background_selected)) {
                    backgroundBorderDisable = a.getColor(styleable.SelectorAttrs_sel_background_selected, -1);
                    colorShapeDrawableSelected.setColor(backgroundBorderDisable);
                }

                if (colorShapeDrawableSelected != null && a.hasValue(styleable.SelectorAttrs_sel_background_border_selected)) {
                    backgroundBorderDisable = a.getColor(styleable.SelectorAttrs_sel_background_border_selected, -1);
                    colorShapeDrawableSelected.setStroke(backgroundBorderWidth, backgroundBorderDisable);
                }

                if (colorShapeDrawableDisable != null && a.hasValue(styleable.SelectorAttrs_sel_background_disable)) {
                    backgroundBorderDisable = a.getColor(styleable.SelectorAttrs_sel_background_disable, -1);
                    colorShapeDrawableDisable.setColor(backgroundBorderDisable);
                }

                if (colorShapeDrawableDisable != null && a.hasValue(styleable.SelectorAttrs_sel_background_border_disable)) {
                    backgroundBorderDisable = a.getColor(styleable.SelectorAttrs_sel_background_border_disable, -1);
                    colorShapeDrawableDisable.setStroke(backgroundBorderWidth, backgroundBorderDisable);
                }

                StateListDrawable stateListDrawable = null;
                Object drawable;
                if (Build.VERSION.SDK_INT < 21 && a.hasValue(styleable.SelectorAttrs_sel_background_ripple)) {
                    drawable = null;
                    if (a.hasValue(styleable.SelectorAttrs_sel_background_ripple_mask)) {
                        drawable = a.getDrawable(styleable.SelectorAttrs_sel_background_ripple_mask);
                        if (drawable instanceof ColorDrawable) {
                            drawable = new GradientDrawable();
                            parseRippleMaskShape((GradientDrawable)drawable, a);
                            ((GradientDrawable)drawable).setColor(a.getColor(styleable.SelectorAttrs_sel_background_ripple, 0));
                        } else {
                            drawable = a.getDrawable(styleable.SelectorAttrs_sel_background_ripple);
                        }
                    } else if (colorShapeDrawable == null && colorShapeDrawablePressed == null) {
                        drawable = a.getDrawable(styleable.SelectorAttrs_sel_background_ripple);
                    } else {
                        GradientDrawable gradientDrawable = new GradientDrawable();
                        gradientDrawable.setShape(shape);
                        gradientDrawable.setCornerRadii(cornerRadii);
                        gradientDrawable.setColor(a.getColor(styleable.SelectorAttrs_sel_background_ripple, 0));
                        drawable = gradientDrawable;
                    }

                    if (colorShapeDrawablePressed == null && bitmapDrawablePressed == null) {
                        if (bitmapDrawable != null || colorShapeDrawable != null) {
                            drawable = new LayerDrawable(new Drawable[]{(Drawable)(bitmapDrawable != null ? bitmapDrawable : colorShapeDrawable), (Drawable)drawable});
                        }
                    } else {
                        drawable = new LayerDrawable(new Drawable[]{(Drawable)(bitmapDrawablePressed != null ? bitmapDrawablePressed : colorShapeDrawablePressed), (Drawable)drawable});
                    }

                    stateListDrawable = new StateListDrawable();
                    stateListDrawable.addState(new int[]{16842910, 16842919}, (Drawable)drawable);
                } else if (colorShapeDrawablePressed != null || bitmapDrawablePressed != null) {
                    drawable = bitmapDrawablePressed != null ? bitmapDrawablePressed : colorShapeDrawablePressed;
                    stateListDrawable = new StateListDrawable();
                    stateListDrawable.addState(new int[]{16842910, 16842919}, (Drawable)drawable);
                }

                if (colorShapeDrawableSelected != null || bitmapDrawableSelected != null) {
                    drawable = bitmapDrawableSelected != null ? bitmapDrawableSelected : colorShapeDrawableSelected;
                    if (stateListDrawable == null) {
                        stateListDrawable = new StateListDrawable();
                    }

                    stateListDrawable.addState(new int[]{16842910, 16842913}, (Drawable)drawable);
                }

                if (colorShapeDrawableDisable != null || bitmapDrawableDisable != null) {
                    drawable = bitmapDrawableDisable != null ? bitmapDrawableDisable : colorShapeDrawableDisable;
                    if (stateListDrawable == null) {
                        stateListDrawable = new StateListDrawable();
                    }

                    stateListDrawable.addState(new int[]{-16842910}, (Drawable)drawable);
                }

                if (bitmapDrawable != null || colorShapeDrawable != null) {
                    if (stateListDrawable == null) {
                        stateListDrawable = new StateListDrawable();
                    }

                    stateListDrawable.addState(new int[0], (Drawable)(bitmapDrawable != null ? bitmapDrawable : colorShapeDrawable));
                }

                boolean hasRipple = parseRipple(view, a, stateListDrawable);
                if (!hasRipple && stateListDrawable != null) {
                    view.setBackgroundDrawable(stateListDrawable);
                }

                a.recycle();
            }
        }
    }

    private static boolean parseRipple(View view, TypedArray a, Drawable content) {
        boolean hasRipple = false;
        if (Build.VERSION.SDK_INT >= 21 && a.hasValue(styleable.SelectorAttrs_sel_background_ripple)) {
            hasRipple = true;
            GradientDrawable maskShapeDrawable = null;
            Drawable maskBitmapDrawable = null;
            if (a.hasValue(styleable.SelectorAttrs_sel_background_ripple_mask)) {
                maskBitmapDrawable = a.getDrawable(styleable.SelectorAttrs_sel_background_ripple_mask);
                if (maskBitmapDrawable instanceof ColorDrawable) {
                    maskBitmapDrawable = null;
                }

                if (maskBitmapDrawable == null) {
                    maskShapeDrawable = new GradientDrawable();
                    parseRippleMaskShape(maskShapeDrawable, a);
                }
            }

            RippleDrawable rippleDrawable = new RippleDrawable(a.getColorStateList(styleable.SelectorAttrs_sel_background_ripple), content, (Drawable)(maskBitmapDrawable != null ? maskBitmapDrawable : maskShapeDrawable));
            view.setBackgroundDrawable(rippleDrawable);
        }

        return hasRipple;
    }

    private static void parseRippleMaskShape(GradientDrawable maskShapeDrawable, TypedArray a) {
        maskShapeDrawable.setColor(a.getColor(styleable.SelectorAttrs_sel_background_ripple_mask, 0));
        int shape;
        if (a.hasValue(styleable.SelectorAttrs_sel_background_ripple_mask_shape)) {
            shape = a.getInt(styleable.SelectorAttrs_sel_background_ripple_mask_shape, 0);
            if (shape == 2) {
                maskShapeDrawable.setShape(GradientDrawable.LINE);
            } else if (shape == 1) {
                maskShapeDrawable.setShape(GradientDrawable.OVAL);
            } else if (shape == 3) {
                maskShapeDrawable.setShape(GradientDrawable.RING);
            } else {
                maskShapeDrawable.setShape(GradientDrawable.RECTANGLE);
            }
        }

        shape = a.getDimensionPixelOffset(styleable.SelectorAttrs_sel_background_ripple_mask_corners, 0);
        maskShapeDrawable.setCornerRadius((float)shape);
        int topLeftRadius = a.getDimensionPixelSize(styleable.SelectorAttrs_sel_background_ripple_mask_corner_topLeft, shape);
        int topRightRadius = a.getDimensionPixelSize(styleable.SelectorAttrs_sel_background_ripple_mask_corner_topRight, shape);
        int bottomLeftRadius = a.getDimensionPixelSize(styleable.SelectorAttrs_sel_background_ripple_mask_corner_bottomLeft, shape);
        int bottomRightRadius = a.getDimensionPixelSize(styleable.SelectorAttrs_sel_background_ripple_mask_corner_bottomRight, shape);
        if (topLeftRadius != shape || topRightRadius != shape || bottomLeftRadius != shape || bottomRightRadius != shape) {
            maskShapeDrawable.setCornerRadii(new float[]{(float)topLeftRadius, (float)topLeftRadius, (float)topRightRadius, (float)topRightRadius, (float)bottomRightRadius, (float)bottomRightRadius, (float)bottomLeftRadius, (float)bottomLeftRadius});
        }

    }
}
