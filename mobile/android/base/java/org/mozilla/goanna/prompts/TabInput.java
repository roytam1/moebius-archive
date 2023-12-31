/* -*- Mode: Java; c-basic-offset: 4; tab-width: 20; indent-tabs-mode: nil; -*-
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.goanna.prompts;

import java.util.LinkedHashMap;

import org.mozilla.goanna.AppConstants.Versions;
import org.mozilla.goanna.R;
import org.mozilla.goanna.util.GoannaBundle;
import org.mozilla.goanna.util.ThreadUtils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

public class TabInput extends PromptInput implements AdapterView.OnItemClickListener {
    public static final String INPUT_TYPE = "tabs";
    public static final String LOGTAG = "GoannaTabInput";

    /* Keeping the order of this in sync with the input is important. */
    final private LinkedHashMap<String, PromptListItem[]> mTabs;

    private TabHost mHost;
    private int mPosition;

    public TabInput(GoannaBundle obj) {
        super(obj);
        mTabs = new LinkedHashMap<String, PromptListItem[]>();
        GoannaBundle[] tabs = obj.getBundleArray("items");
        for (int i = 0; i < (tabs != null ? tabs.length : 0); i++) {
            GoannaBundle tab = tabs[i];
            String title = tab.getString("label");
            GoannaBundle[] items = tab.getBundleArray("items");
            mTabs.put(title, PromptListItem.getArray(items));
        }
    }

    @Override
    public View getView(final Context context) throws UnsupportedOperationException {
        final LayoutInflater inflater = LayoutInflater.from(context);
        mHost = (TabHost) inflater.inflate(R.layout.tab_prompt_input, null);
        mHost.setup();

        for (String title : mTabs.keySet()) {
            final TabHost.TabSpec spec = mHost.newTabSpec(title);
            spec.setContent(new TabHost.TabContentFactory() {
                @Override
                public View createTabContent(final String tag) {
                    PromptListAdapter adapter = new PromptListAdapter(context, android.R.layout.simple_list_item_1, mTabs.get(tag));
                    ListView listView = new ListView(context);
                    listView.setCacheColorHint(0);
                    listView.setOnItemClickListener(TabInput.this);
                    listView.setAdapter(adapter);
                    return listView;
                }
            });

            spec.setIndicator(title);
            mHost.addTab(spec);
        }
        mView = mHost;
        return mHost;
    }

    @Override
    public Object getValue() {
        final GoannaBundle obj = new GoannaBundle(2);
        obj.putInt("tab", mHost.getCurrentTab());
        obj.putInt("item", mPosition);
        return obj;
    }

    @Override
    public boolean getScrollable() {
        return true;
    }

    @Override
    public boolean canApplyInputStyle() {
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ThreadUtils.assertOnUiThread();
        mPosition = position;
        notifyListeners(Integer.toString(position));
    }

}
