package romain.guarnotta.channelmessaging.Helper;

import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

import romain.guarnotta.channelmessaging.Model.Channel;

/**
 * Created by romain on 07/03/16.
 */
public class ChannelFilter extends Filter {
    private final ListViewAdapterForChannel mAdapter;
    private List<Channel> mChannels;

    public ChannelFilter(List<Channel> originalList, ListViewAdapterForChannel adapter) {
        this.mChannels = originalList;
        this.mAdapter = adapter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        String filterString = constraint.toString().toLowerCase();
        FilterResults results = new FilterResults();

        int count = mChannels.size();
        final ArrayList<Channel> nlist = new ArrayList<Channel>(count);
        Channel filterableChannel ;

        for (int i = 0; i < count; i++) {
            filterableChannel = mChannels.get(i);
            if (filterableChannel.getName().toLowerCase().contains(filterString)) {
                nlist.add(filterableChannel);
            }
        }

        results.values = nlist;
        results.count = nlist.size();

        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        mAdapter.filteredChannels = (ArrayList<Channel>)results.values;
        mAdapter.notifyDataSetChanged();
    }
}
