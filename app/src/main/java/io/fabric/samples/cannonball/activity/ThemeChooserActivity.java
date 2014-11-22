/**
 * Copyright (C) 2014 Twitter Inc and other contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.fabric.samples.cannonball.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.twitter.sdk.android.Twitter;

import io.fabric.samples.cannonball.App;
import io.fabric.samples.cannonball.R;
import io.fabric.samples.cannonball.model.Theme;
import io.fabric.samples.cannonball.view.ThemeAdapter;

public class ThemeChooserActivity extends Activity {
    public static final String IS_NEW_POEM = "ThemeChooser.IS_NEW_POEM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_chooser);
        setUpViews();
    }

    private void setUpViews() {
        setUpHistory();
        setUpPopular();
        setUpIcon();
        setUpThemes();
    }

    private void setUpIcon() {
        final ImageView icon = (ImageView) findViewById(R.id.cannonball_logo);
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Crashlytics.log("ThemeChooser: clicked About button");
                final Intent intent = new Intent(ThemeChooserActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setUpPopular() {
        final ImageView popular = (ImageView) findViewById(R.id.popular);
        popular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Crashlytics.log("ThemeChooser: clicked Popular button");
                // Crashlytics:
                // A Crash will happen if you try to access Popular poems page without having an
                // activated account
                Intent intent = new Intent(ThemeChooserActivity.this, PoemPopularActivity.class);
                if (Twitter.getSessionManager().getActiveSession() == null &&
                        !App.getInstance().areCrashesEnabled()) {
                    Crashlytics.log("ThemeChooser: An enabled crash will execute");
                    Toast.makeText(ThemeChooserActivity.this, getResources().getString(R.string
                            .you_need_twitter), Toast.LENGTH_LONG).show();
                    intent = new Intent(ThemeChooserActivity.this, LoginActivity.class);
                }
                startActivity(intent);
            }
        });
    }

    private void setUpHistory() {
        final ImageView history = (ImageView) findViewById(R.id.history);
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Crashlytics.log("ThemeChooser: clicked History button");
                final Intent intent = new Intent(ThemeChooserActivity.this,
                        PoemHistoryActivity.class);
                intent.putExtra(IS_NEW_POEM, false);
                startActivity(intent);
            }
        });
    }

    private void setUpThemes() {
        final ListView view = (ListView) findViewById(R.id.theme_list);
        view.setAdapter(new ThemeAdapter(this, Theme.values()));
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Theme theme = Theme.values()[position];
                Crashlytics.log("ThemeChooser: clicked on Theme: " + theme.getDisplayName());
                Crashlytics.setString(App.CRASHLYTICS_KEY_THEME, theme.getDisplayName());
                final Intent intent = new Intent(getBaseContext(), PoemBuilderActivity.class);
                intent.putExtra(PoemBuilderActivity.KEY_THEME, theme);
                startActivity(intent);
            }
        });
    }
}
