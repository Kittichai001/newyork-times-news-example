package chai.nytimesnewsexample;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import chai.nytimesnewsexample.databinding.RowItemBinding;

/**
 * Created by chai on 8/6/18.
 */

public class ListAdapter extends BaseAdapter implements Filterable {

    List<String> mData;
    List<String> mStringFilterList;
    List<String> iconList;
    ValueFilter valueFilter;
    Context context;
    private LayoutInflater inflater;

    public ListAdapter(List<String> mData, List<String> iconList, Context context) {
        this.mData =mData;
        mStringFilterList = mData;
        this.iconList = iconList;
        this.context = context;
    }


    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public String getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        if (inflater == null) {
            inflater = (LayoutInflater) parent.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        RowItemBinding rowItemBinding = DataBindingUtil.inflate(inflater, R.layout.row_item, parent, false);
        rowItemBinding.stringName.setText(mData.get(position));
        if(iconList.get(position) != null)
            Picasso.with(context).load(iconList.get(position)).fit().into(rowItemBinding.imView);
        else
            rowItemBinding.imView.setImageResource(R.mipmap.ic_launcher);
        return rowItemBinding.getRoot();
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                List<String> filterList = new ArrayList<>();
                for (int i = 0; i < mStringFilterList.size(); i++) {
                    if ((mStringFilterList.get(i).toUpperCase()).contains(constraint.toString().toUpperCase())) {
                        filterList.add(mStringFilterList.get(i));
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = mStringFilterList.size();
                results.values = mStringFilterList;
            }
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            mData = (List<String>) results.values;
            notifyDataSetChanged();
        }

    }
}
