/*******************************************************************************
 * Copyright (c) 2010 Denis Solonenko.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * Contributors:
 *     Denis Solonenko - initial API and implementation
 ******************************************************************************/
package ru.orangesoftware.financisto.activity;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import ru.orangesoftware.financisto.R;
import ru.orangesoftware.financisto.adapter.EntityEnumAdapter;
import ru.orangesoftware.financisto.fragment.AccountListFragment_;
import ru.orangesoftware.financisto.fragment.BlotterFragment_;
import ru.orangesoftware.financisto.utils.EntityEnum;

@EActivity(R.layout.main2)
public class MainActivity2 extends FragmentActivity {

    @ViewById(R.id.pager)
    protected ViewPager pager;

    @ViewById(R.id.drawer_layout)
    protected DrawerLayout drawerLayout;

    @ViewById(R.id.pager_strip)
    protected PagerTabStrip pagerTabStrip;

    @ViewById(R.id.left_drawer)
    protected ListView drawer;

    protected ActionBarDrawerToggle drawerToggle;

    @AfterViews
    protected void afterViews() {
        drawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle(R.string.app_name);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle(R.string.app_name);
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);

        EntityEnumAdapter<DrawerItem> adapter = new EntityEnumAdapter<DrawerItem>(this, R.layout.drawer_list_item, DrawerItem.values());
        drawer.setAdapter(adapter);
        drawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onDrawerItemClicked(position);
            }
        });

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        pagerTabStrip.setDrawFullUnderline(true);
        AppSectionsPagerAdapter pagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager(), this);
        pager.setAdapter(pagerAdapter);
    }

    private void onDrawerItemClicked(int position) {
        drawer.setItemChecked(position, true);
        drawerLayout.closeDrawer(drawer);
        DrawerItem item = DrawerItem.values()[position];
        item.onClick(this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

        private final Context context;

        public AppSectionsPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return AccountListFragment_.builder().build();
                default:
                    return BlotterFragment_.builder().build();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return context.getString(R.string.accounts);
                case 1:
                    return context.getString(R.string.blotter);
                case 2:
                    return context.getString(R.string.budgets);
                case 3:
                    return context.getString(R.string.reports);
            }
            return null;
        }
    }

    private static enum DrawerItem implements EntityEnum {
        ENTITIES(R.string.entities, R.drawable.drawer_action_list){
            @Override
            public void onClick(final Context context) {
                EntityListActivity_.intent(context).start();
            }
        };

        private final int titleId;
        private final int iconId;

        DrawerItem(int titleId, int iconId) {
            this.titleId = titleId;
            this.iconId = iconId;
        }

        @Override
        public int getTitleId() {
            return titleId;
        }

        @Override
        public int getIconId() {
            return iconId;
        }

        public abstract void onClick(Context context);
    }

}