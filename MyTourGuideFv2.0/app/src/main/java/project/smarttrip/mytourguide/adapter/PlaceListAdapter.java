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
import project.smarttrip.mytourguide.model.PlaceItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by invite on 14/06/15.
 */
public class PlaceListAdapter extends ArrayAdapter<PlaceItem> implements Filterable {

    private Context context;
    private List<PlaceItem> listPlaces;
    private List<PlaceItem> origListPlaces;
    private LayoutInflater inflater;
    private Filter placeListFilter;

    public PlaceListAdapter(Context context, List<PlaceItem> listPlaces) {
        super(context,R.layout.list_item,listPlaces);
        this.listPlaces = listPlaces;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.origListPlaces = listPlaces;
    }

    public int getCount() {
        return listPlaces.size();
    }

    public PlaceItem getItem(int position) {
        return listPlaces.get(position);
    }

    public long getItemId(int position) {
        return listPlaces.get(position).hashCode();
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = new ViewHolder();

        if(convertView == null) {

            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            //convertView = this.inflater.inflate(R.layout.list_item, parent, false);
            convertView = this.inflater.inflate(R.layout.list_item, null);

            holder.txtName = (TextView) convertView.findViewById(R.id.name_value);
            holder.txtDescr = (TextView) convertView.findViewById(R.id.descr_value);
            holder.txtOpen = (TextView) convertView.findViewById(R.id.open_value);
            holder.txtLongitude = (TextView) convertView.findViewById(R.id.longitude_value);
            holder.txtLatitude = (TextView) convertView.findViewById(R.id.latitude_value);
            holder.txtId = (TextView) convertView.findViewById(R.id.id_value);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        PlaceItem placeItem = listPlaces.get(position);

        holder.txtName.setText(placeItem.getTitle());
        holder.txtDescr.setText(placeItem.getDescription());
        holder.txtOpen.setText(placeItem.getOpen());
        holder.txtLongitude.setText(placeItem.getLongitude());
        holder.txtLatitude.setText(placeItem.getLatitude());
        holder.txtId.setText(placeItem.getId());

        holder.txtLongitude.setVisibility(View.GONE);
        holder.txtLatitude.setVisibility(View.GONE);
        holder.txtId.setVisibility(View.GONE);

        return convertView;

    }

    private class ViewHolder {
        TextView txtName;
        TextView txtDescr;
        TextView txtOpen;
        TextView txtLongitude;
        TextView txtLatitude;
        TextView txtId;
    }

    public void resetData(){
        listPlaces = origListPlaces;
    }

    @Override
    public Filter getFilter() {
        if(placeListFilter == null)
            placeListFilter = new PlaceListFilter();

        return placeListFilter;
    }


    private class PlaceListFilter extends Filter {

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

}
