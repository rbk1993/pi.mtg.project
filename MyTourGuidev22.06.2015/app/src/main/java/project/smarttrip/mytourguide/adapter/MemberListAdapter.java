package project.smarttrip.mytourguide.adapter;


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

import project.smarttrip.mytourguide.R;
import project.smarttrip.mytourguide.model.MemberItem;

/**
 * Created by invite on 17/06/15.
 */
public class MemberListAdapter extends ArrayAdapter<MemberItem> implements Filterable {

    private Context context;
    private List<MemberItem> listMembers;
    private List<MemberItem> origListMembers;
    private LayoutInflater inflater;
    private Filter memberListFilter;

    public MemberListAdapter(Context context, List<MemberItem> listMembers) {
        super(context, R.layout.list_members,listMembers);
        this.listMembers = listMembers;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.origListMembers = listMembers;
    }

    public int getCount() {
        return listMembers.size();
    }

    public MemberItem getItem(int position) {
        return listMembers.get(position);
    }

    public long getItemId(int position) {
        return listMembers.get(position).hashCode();
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = new ViewHolder();

        if(convertView == null) {

            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            //convertView = this.inflater.inflate(R.layout.list_item, parent, false);
            convertView = this.inflater.inflate(R.layout.list_members, null);

            holder.txtNom = (TextView) convertView.findViewById(R.id.nom_membre_value);
            holder.txtPrenom = (TextView) convertView.findViewById(R.id.prenom_membre_value);
            holder.txtBirthday = (TextView) convertView.findViewById(R.id.birthday_value);
            holder.txtId = (TextView) convertView.findViewById(R.id.id_membre_value);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        MemberItem memberItem = listMembers.get(position);

        holder.txtNom.setText(memberItem.getNom());
        holder.txtPrenom.setText(memberItem.getPrenom());
        holder.txtBirthday.setText(memberItem.getBirthday());
        holder.txtId.setText(memberItem.getId());

        holder.txtId.setVisibility(View.GONE);

        return convertView;

    }

    private class ViewHolder {
        TextView txtNom;
        TextView txtPrenom;
        TextView txtBirthday;
        TextView txtId;
    }

    public void resetData(){
        listMembers = origListMembers;
    }

    @Override
    public Filter getFilter() {
        if(memberListFilter == null)
            memberListFilter = new PlaceListFilter();

        return memberListFilter;
    }


    private class PlaceListFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint){
            FilterResults results = new FilterResults();
            // We implement here the filter logic
            if(constraint == null || constraint.length() == 0) {
                //  No filter implementend we return all the list
                results.values = origListMembers;
                results.count = origListMembers.size();
            } else {
                //We perform filtering operation
                List<MemberItem> nListMembers = new ArrayList<MemberItem>();

                for (MemberItem p : listMembers) {
                    if (p.getNom().toUpperCase().startsWith(constraint.toString().toUpperCase())
                            || p.getPrenom().toUpperCase().startsWith(constraint.toString().toUpperCase()))
                        nListMembers.add(p);
                }

                results.values = nListMembers;
                results.count = nListMembers.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results){

            //Now we have to inform the adapter about the new list filtered
            if (results.count == 0)
                notifyDataSetInvalidated();
            else {
                listMembers = (List<MemberItem>) results.values;
                notifyDataSetChanged();
            }

        }
    }

}

