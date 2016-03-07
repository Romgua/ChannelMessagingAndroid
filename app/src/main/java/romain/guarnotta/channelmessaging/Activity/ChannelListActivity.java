package romain.guarnotta.channelmessaging.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
    public void setDefaultChannel(int channelID) {
        MessageFragment messageFragment = (MessageFragment)getSupportFragmentManager().findFragmentById(R.id.message_fragment_id);
        if (messageFragment != null && messageFragment.isInLayout()) {
            messageFragment.sChannelID = channelID;
            messageFragment.refresh();
        }
    }
}
