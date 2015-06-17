package project.smarttrip.mytourguide.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import project.smarttrip.mytourguide.R;
import project.smarttrip.mytourguide.model.GroupItem;
import project.smarttrip.mytourguide.model.PlaceItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by invite on 14/06/15.
 */
public class GroupListAdapter extends ArrayAdapter<GroupItem> {

    private Context context;
    private List<GroupItem> listGroups;
    private List<GroupItem> origListGroups;
    private LayoutInflater inflater;
    private Filter groupListFilter;

    public GroupListAdapter(Context context, List<GroupItem> listGroups) {
        super(context,R.layout.list_groups,listGroups);
        this.listGroups = listGroups;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.origListGroups = listGroups;
    }

    public int getCount() {
        return listGroups.size();
    }

    public GroupItem getItem(int position) {
        return listGroups.get(position);
    }

    public long getItemId(int position) {
        return listGroups.get(position).hashCode();
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = new ViewHolder();

        if(convertView == null) {

            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            //convertView = this.inflater.inflate(R.layout.list_groups, parent, false);
            convertView = this.inflater.inflate(R.layout.list_groups, null);

            holder.txtName = (TextView) convertView.findViewById(R.id.name_value);
            holder.txtDate = (TextView) convertView.findViewById(R.id.date_value);
            holder.txtHoraire = (TextView) convertView.findViewById(R.id.horaire_value);
            holder.txtDuree = (TextView) convertView.findViewById(R.id.duree_value);
            holder.txtAdminId = (TextView) convertView.findViewById(R.id.admin_id_value);
            holder.txtId = (TextView) convertView.findViewById(R.id.id_value);
            holder.txtMembresInscrits = (TextView) convertView.findViewById(R.id.nombre_membres_inscrits_value);
            holder.txtMembresMax = (TextView) convertView.findViewById(R.id.nombre_membres_max_value);
            holder.txtUrl = (TextView) convertView.findViewById(R.id.url_value);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        GroupItem groupItem = listGroups.get(position);

        holder.txtName.setText(groupItem.getName());
        holder.txtDate.setText(groupItem.getDate());
        holder.txtHoraire.setText(groupItem.getHoraire());
        holder.txtDuree.setText(groupItem.getDuree());

        holder.txtAdminId.setText(groupItem.getAdmin());
        holder.txtId.setText(groupItem.getId());
        holder.txtMembresInscrits.setText(groupItem.getMembresinscrits());
        holder.txtMembresMax.setText(groupItem.getMembresmax());
        holder.txtUrl.setText(groupItem.getUrl());

        holder.txtAdminId.setVisibility(View.GONE);
        holder.txtId.setVisibility(View.GONE);
        holder.txtMembresMax.setVisibility(View.GONE);
        holder.txtMembresInscrits.setVisibility(View.GONE);
        holder.txtUrl.setVisibility(View.GONE);

        return convertView;

    }

    private class ViewHolder {
        TextView txtName;
        TextView txtDate;
        TextView txtHoraire;
        TextView txtDuree;
        TextView txtAdminId;
        TextView txtId;
        TextView txtMembresMax;
        TextView txtMembresInscrits;
        TextView txtUrl;
    }

    public void resetData(){
        listGroups = origListGroups;
    }


    /*
    @Override
    public Filter getFilter() {
        if(placeListFilter == null)
            placeListFilter = new PlaceListFilter();

        return placeListFilter;
    }
    */

    /*
    private class GroupListFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint){
            FilterResults results = new FilterResults();
            // We implement here the filter logic
            if(constraint == null || constraint.length() == 0) {
                //  No filter implementend we return all the list
                results.values = origListPlaces;
                results.count = origListPlaces.size();
            } else {
                //We perform filtering operation
                List<PlaceItem> nListPlaces = new ArrayList<PlaceItem>();

                for (PlaceItem p : listPlaces) {
                    if (p.getDescription().toUpperCase().startsWith(constraint.toString().toUpperCase())
                            || p.getTitle().toUpperCase().startsWith(constraint.toString().toUpperCase()))
                        nListPlaces.add(p);
                }

                results.values = nListPlaces;
                results.count = nListPlaces.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results){

            //Now we have to inform the adapter about the new list filtered
            if (results.count == 0)
                notifyDataSetInvalidated();
            else {
                listPlaces = (List<PlaceItem>) results.values;
                notifyDataSetChanged();
            }

        }
    }
    */

}