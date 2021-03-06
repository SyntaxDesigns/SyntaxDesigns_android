package edu.uni.cs.syntaxdesigns.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import edu.uni.cs.syntaxdesigns.R;
import edu.uni.cs.syntaxdesigns.VOs.PhraseResults;
import edu.uni.cs.syntaxdesigns.view.RatingsView;

import java.util.List;

public class BrowseRecipesListAdapter extends BaseArrayAdapter {

    private List<PhraseResults> mResults;
    private Resources mResources;

    public BrowseRecipesListAdapter(Context context, List<PhraseResults> results) {
        super(context, 0, results);

        mResults = results;
        mResources = context.getResources();

        mInflater = LayoutInflater.from(context);
    }

    private static class ViewHolder {
        TextView numberOfIngredients;
        TextView recipeName;
        RatingsView rating;
        TextView timeToCook;
        ImageView recipeImage;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.browse_recipes_list_row, parent, false);

            viewHolder = new ViewHolder();

            viewHolder.recipeName = (TextView) convertView.findViewById(R.id.recipe_name);
            viewHolder.rating = (RatingsView) convertView.findViewById(R.id.rating);
            viewHolder.numberOfIngredients = (TextView) convertView.findViewById(R.id.number_of_ingredients);
            viewHolder.timeToCook = (TextView) convertView.findViewById(R.id.time_to_cook);
            viewHolder.recipeImage = (ImageView) convertView.findViewById(R.id.recipe_image);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        PhraseResults results = mResults.get(position);

        viewHolder.recipeName.setText(results.recipeName);
        viewHolder.rating.setRating(results.rating);
        viewHolder.numberOfIngredients.setText(" " + Integer.toString(results.ingredients.size()));
        viewHolder.timeToCook.setText(" " + Integer.toString(results.totalTimeInSeconds / 60) + " ");

        if (results.smallImageUrls != null) {

            Picasso.with(getContext())
                   .load(results.smallImageUrls.get(0))
                   .placeholder(android.R.drawable.ic_menu_gallery)
                   .error(android.R.drawable.ic_menu_report_image)
                   .fit()
                   .centerCrop()
                   .into(viewHolder.recipeImage);
        } else {
            Picasso.with(getContext()).load(R.drawable.ic_launcher)
                   .fit()
                   .centerCrop()
                   .into(viewHolder.recipeImage);
        }

        return convertView;
    }

    @Override
    public PhraseResults getItem(int position) {
        return mResults.get(position);
    }
}
