package org.mozilla.vrbrowser.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.mozilla.vrbrowser.R;
import org.mozilla.vrbrowser.ui.widgets.WidgetPlacement;

import java.util.List;


public class LanguageSelectorView extends FrameLayout {
    public static class Item {
        public final String title;
        public final Object tag;

        public Item(String aTitle, Object aTag) {
            this.title = aTitle;
            this.tag = aTag;
        }
    }

    public interface Delegate {
        void onLanguageClick(Item aItem);
    }

    private GridLayout mLangRowContainer;
    private Delegate mDelegate;
    private List<Item> mItems;
    private int mItemWidth;
    private static final int kMaxItemsPerColumn = 4;

    public LanguageSelectorView(@NonNull Context context) {
        super(context);
        initialize();
    }

    public LanguageSelectorView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public LanguageSelectorView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    private void initialize() {
        inflate(getContext(), R.layout.language_selection, this);
        mLangRowContainer = findViewById(R.id.langRowContainer);
        mItemWidth = WidgetPlacement.pixelDimension(getContext(), R.dimen.lang_selector_item_width);
    }

    public void setDelegate(Delegate aDelegate) {
        mDelegate = aDelegate;
    }

    public void setItems(List<Item> aItems) {
        mItems = aItems;
        mLangRowContainer.removeAllViews();
        int columns = (aItems.size() / kMaxItemsPerColumn) + 1;
        int rows = aItems.size() < kMaxItemsPerColumn ? aItems.size() : kMaxItemsPerColumn;
        mLangRowContainer.setColumnCount(columns);
        mLangRowContainer.setRowCount(rows);

        for (Item item: aItems) {
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = mItemWidth;
            mLangRowContainer.addView(createLangButton(item), params);
        }
    }

    public List<Item> getItems() {
        return mItems;
    }

    private OnClickListener clickHandler = v -> {
        UITextButton button = (UITextButton) v;
        if (mDelegate != null) {
            mDelegate.onLanguageClick((Item)button.getTag());
        }
    };

    private UITextButton createLangButton(Item aItem) {
        UITextButton button = new UITextButton(getContext());
        button.setTintColorList(R.drawable.lang_selector_button_color);
        button.setBackground(getContext().getDrawable(R.drawable.lang_selector_button_background));
        button.setOnClickListener(clickHandler);
        button.setPadding(13, 13, 13, 13);
        button.setIncludeFontPadding(false);
        button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        button.setText(aItem.title);
        button.setTextAlignment(TEXT_ALIGNMENT_VIEW_START);
        button.setTag(aItem);

        return button;
    }
}
