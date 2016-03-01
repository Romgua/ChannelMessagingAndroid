package romain.guarnotta.channelmessaging;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by romain on 29/02/16.
 */
public class ListViewAdapterForMessage extends ArrayAdapter<List> {
    private final Context context;
    private final List<Message> messages;

    public ListViewAdapterForMessage(Context context, List messages) {
        super(context, R.layout.row_message, messages);
        this.context = context;
        this.messages = messages;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater =
                (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_message, parent, false);

        ImageView iv_image_profil = (ImageView)rowView.findViewById(R.id.iv_image_profil);
        TextView tv_message = (TextView)rowView.findViewById(R.id.tv_message);
        TextView tv_date = (TextView)rowView.findViewById(R.id.tv_date);

//        new CircleTransformation(iv_image_profil, messages.get(position).getImageUrl()).execute();
        Picasso
                .with(context)
                .load(messages.get(position).getImageUrl())
                .transform(new CircleTransformation())
                .into(iv_image_profil);
        tv_message.setText(messages.get(position).getMessage());
        tv_date.setText(messages.get(position).getDate());

        return rowView;
    }
}
