package romain.guarnotta.channelmessaging.Helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import romain.guarnotta.channelmessaging.Model.Channel;
import romain.guarnotta.channelmessaging.R;

/**
 * Created by romain on 08/02/16.
 */
public class ListViewAdapterForChannel extends ArrayAdapter<List> implements Filterable {
    private final Context context;
    private final List<Channel> channels;
    List<Channel> filteredChannels;
    private ChannelFilter mFilter;

    public ListViewAdapterForChannel(Context context, List channels) {
        super(context, R.layout.row_channel, channels);
        this.context = context;
        this.channels = channels;
        this.filteredChannels = channels;
        this.mFilter = new ChannelFilter(channels,this);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater =
                (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_channel, parent, false);

        TextView tv_title_channel = (TextView)rowView.findViewById(R.id.tv_title_channel);
        TextView tv_connectusers = (TextView)rowView.findViewById(R.id.tv_connectusers);

        tv_title_channel.setText(filteredChannels.get(position).getName());
        tv_connectusers.setText("Nombre d'utilisateurs connectés : "+filteredChannels.get(position).getConnectedusers());

        rowView.setTag(filteredChannels.get(position));

        return rowView;
    }

    @Override
    public int getCount() {
        return filteredChannels.size();
    }

    public void setFilter(String filterString) {
        mFilter.filter(filterString);
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    public void resetResearchFilter() {
        filteredChannels = channels;
    }
}
