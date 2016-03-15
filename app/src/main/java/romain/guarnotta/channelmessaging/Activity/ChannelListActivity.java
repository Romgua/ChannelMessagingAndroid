package romain.guarnotta.channelmessaging.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import romain.guarnotta.channelmessaging.Fragment.ChannelListFragment;
import romain.guarnotta.channelmessaging.Fragment.MessageFragment;
import romain.guarnotta.channelmessaging.Fragment.OnChannelListFragmentUpdate;
import romain.guarnotta.channelmessaging.Model.Channel;
import romain.guarnotta.channelmessaging.R;

/**
 * Created by romain on 08/02/16.
 */
public class ChannelListActivity extends GPSActivity
        implements AdapterView.OnItemClickListener, OnChannelListFragmentUpdate {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_channel);

        String channelID = getIntent().getStringExtra("channelID");
        if (channelID != null) {
            Channel channel = new Channel();
            channel.setChannelID(Integer.valueOf(channelID));
            goToMessageFragment(channel);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_basic, menu);

        // SearchView
        final MenuItem itemSearch = menu.findItem(R.id.action_search);
        final SearchView mSearchView = (SearchView)itemSearch.getActionView();
        final ChannelListFragment channelFragment =
                (ChannelListFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.channel_list_fragment_id);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!newText.equalsIgnoreCase("")) {
                    channelFragment.searchStringInList(newText);
                }
                return false;
            }
        });

        // Get the search close button image view
        ImageView closeButton = (ImageView)mSearchView.findViewById(R.id.search_close_btn);
        // Set on click listener
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et = (EditText) findViewById(R.id.search_src_text);
                et.setText("");
                //Clear query
                mSearchView.setQuery("", false);
                //Collapse the action view
                mSearchView.onActionViewCollapsed();
                //Collapse the search widget
                itemSearch.collapseActionView();
                channelFragment.resetResearchFilter();
            }
        });

        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Channel channel = (Channel)view.getTag();
        ChannelListFragment channelListFragment = (ChannelListFragment)getSupportFragmentManager().findFragmentById(R.id.channel_list_fragment_id);

        goToMessageFragment(channel);
    }

    public void goToMessageFragment(Channel channel) {
        MessageFragment messageFragment = (MessageFragment)getSupportFragmentManager().findFragmentById(R.id.message_fragment_id);

        int channelID = channel.getChannelID();
        if (messageFragment == null || !messageFragment.isInLayout()) {
            Intent i = new Intent(this, MessageActivity.class);
            i.putExtra("channelID", channelID);
            startActivity(i);
        } else {
            messageFragment.iChannelID = channelID;
            messageFragment.refresh();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                // Do Nothing
                return true;
            case R.id.action_settings:
                // Do Nothing
                return true;
            case R.id.action_deco:
                LoginActivity.logOut();
                finish();
                return true;
            case R.id.action_infos:
                Toast.makeText(this, "Application created by Romain G.", Toast.LENGTH_LONG).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setDefaultChannel(int channelID) {
        MessageFragment messageFragment = (MessageFragment)getSupportFragmentManager().findFragmentById(R.id.message_fragment_id);
        if (messageFragment != null && messageFragment.isInLayout()) {
            messageFragment.iChannelID = channelID;
            messageFragment.refresh();
        }
    }
}
