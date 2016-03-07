package romain.guarnotta.channelmessaging.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import romain.guarnotta.channelmessaging.Fragment.ChannelListFragment;
import romain.guarnotta.channelmessaging.Fragment.MessageFragment;
import romain.guarnotta.channelmessaging.Fragment.OnChannelListFragmentUpdate;
import romain.guarnotta.channelmessaging.Model.Channel;
import romain.guarnotta.channelmessaging.R;

/**
 * Created by romain on 08/02/16.
 */
public class ChannelListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, OnChannelListFragmentUpdate {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_channel);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_basic, menu);

        // SearchView
        MenuItem itemSearch = menu.findItem(R.id.action_search);
        SearchView mSearchView = (SearchView)itemSearch.getActionView();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Channel channel = (Channel)view.getTag();
        ChannelListFragment channelListFragment = (ChannelListFragment)getSupportFragmentManager().findFragmentById(R.id.channel_list_fragment_id);
        MessageFragment messageFragment = (MessageFragment)getSupportFragmentManager().findFragmentById(R.id.message_fragment_id);

        if (messageFragment == null || !messageFragment.isInLayout()) {
            Intent i = new Intent(this, MessageActivity.class);
            i.putExtra("channelID", channel.getChannelID());
            startActivity(i);
        } else {
            messageFragment.sChannelID = channel.getChannelID();
            messageFragment.refresh();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                // Do something
                return true;
            case R.id.action_settings:
                // Do something
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
            messageFragment.sChannelID = channelID;
            messageFragment.refresh();
        }
    }
}
