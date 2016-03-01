package romain.guarnotta.channelmessaging.Helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import romain.guarnotta.channelmessaging.Model.Channel;
import romain.guarnotta.channelmessaging.R;

/**
 * Created by romain on 08/02/16.
 */
public class ListViewAdapterForChannel extends ArrayAdapter<List> {
    private final Context context;
    private final List<Channel> channels;

    public ListViewAdapterForChannel(Context context, List channels) {
        super(context, R.layout.row_channel, channels);
        this.context = context;
        this.channels = channels;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater =
                (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_channel, parent, false);
        TextView tv_title_channel = (TextView)rowView.findViewById(R.id.tv_title_channel);
        TextView tv_connectusers = (TextView)rowView.findViewById(R.id.tv_connectusers);
        tv_title_channel.setText(channels.get(position).getName());
        tv_connectusers.setText("Nombre d'utilisateurs connectés : "+channels.get(position).getConnectedusers());

        return rowView;
    }
}
