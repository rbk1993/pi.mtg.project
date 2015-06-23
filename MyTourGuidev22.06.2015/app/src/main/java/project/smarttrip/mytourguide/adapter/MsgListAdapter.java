package project.smarttrip.mytourguide.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import project.smarttrip.mytourguide.R;
import project.smarttrip.mytourguide.model.MsgItem;

/**
 * Created by invite on 17/06/15.
 */
public class MsgListAdapter extends ArrayAdapter<MsgItem> {

    private Context context;
    private List<MsgItem> listMsg;
    private LayoutInflater inflater;

    public MsgListAdapter(Context context, List<MsgItem> listMsg) {
        super(context, R.layout.list_msg, listMsg);
        this.listMsg = listMsg;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public int getCount() {
        return listMsg.size();
    }

    public MsgItem getItem(int position) {
        return listMsg.get(position);
    }

    public long getItemId(int position) {
        return listMsg.get(position).hashCode();
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = new ViewHolder();

        if (convertView == null) {

            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            //convertView = this.inflater.inflate(R.layout.list_item, parent, false);
            convertView = this.inflater.inflate(R.layout.list_msg, null);

            holder.txtLogin = (TextView) convertView.findViewById(R.id.login_msg_value);
            holder.txtMsg = (TextView) convertView.findViewById(R.id.msg_value);
            holder.txtTimestamp = (TextView) convertView.findViewById(R.id.timestamp_msg_value);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        MsgItem msgItem = listMsg.get(position);

        holder.txtMsg.setText(msgItem.getMsg());
        holder.txtLogin.setText(msgItem.getLogin());
        holder.txtTimestamp.setText(msgItem.getTimestamp());

        return convertView;

    }

    private class ViewHolder {
        TextView txtMsg;
        TextView txtLogin;
        TextView txtTimestamp;

    }
}