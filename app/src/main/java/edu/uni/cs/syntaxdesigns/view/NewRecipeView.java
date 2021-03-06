package edu.uni.cs.syntaxdesigns.view;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import edu.uni.cs.syntaxdesigns.Adapters.RecipeDialogIngredientsListAdapter;
import edu.uni.cs.syntaxdesigns.R;
import edu.uni.cs.syntaxdesigns.VOs.PhraseResults;
import edu.uni.cs.syntaxdesigns.application.SyntaxDesignsApplication;
import edu.uni.cs.syntaxdesigns.database.cursor.RecipeCursor;
import edu.uni.cs.syntaxdesigns.database.dao.IngredientsDao;
import edu.uni.cs.syntaxdesigns.database.dao.RecipeDao;

import javax.inject.Inject;

public class NewRecipeView extends LinearLayout {

    private PhraseResults mResults;

    private ImageView mCloseDialog;
    private TextView mRecipeName;
    private ImageView mRecipeImage;
    private Button mSaveRecipe;
    private Context mContext;
    private RatingsView mRating;
    private TextView mTime;
    private ListView mListView;
    private Button mViewDirections;
    private RecipeDialogIngredientsListAdapter mAdapter;

    private DetailsListener mCallback;

    private Resources mResources;

    @Inject RecipeDao mRecipeDao;
    @Inject IngredientsDao mIngredientsDao;

    public NewRecipeView(Context context, PhraseResults results) {
        super(context);

        mResults = results;
        initialize(context);
    }

    public NewRecipeView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initialize(context);
    }

    private void initialize(final Context context) {
        LayoutInflater.from(context).inflate(R.layout.recipe_dialog_view, this);

        mContext = context;
        mResources = mContext.getResources();
        SyntaxDesignsApplication.inject(this);

        mCloseDialog = (ImageView) findViewById(R.id.close_dialog);
        mRecipeName = (TextView) findViewById(R.id.recipe_name);
        mListView = (ListView) findViewById(R.id.ingredients_list);
        mTime = (TextView) findViewById(R.id.time_in_minutes);
        mRating = (RatingsView) findViewById(R.id.rating_by_stars);
        mViewDirections = (Button) findViewById(R.id.view_directions);
        mSaveRecipe = (Button) findViewById(R.id.save_recipe);
        mRecipeImage = (ImageView) findViewById(R.id.recipe_image);

        Picasso.with(context).load(mResults.smallImageUrls.get(0))
               .placeholder(R.drawable.ic_launcher)
               .error(R.drawable.ic_launcher)
               .fit()
               .centerCrop()
               .into(mRecipeImage);

        setTextViews();

        initializeListView();

        mCloseDialog.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.closeDialog();
            }
        });

        mViewDirections.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.startRecipeDetails(mResults.id);
            }
        });


        mSaveRecipe.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRecipe(mResults);
            }
        });
    }

    public void setCallback(DetailsListener callback) {
        mCallback = callback;
    }

    private void initializeListView() {
        mAdapter = new RecipeDialogIngredientsListAdapter(mContext, mResults.ingredients);
        mListView.setAdapter(mAdapter);
    }

    private void setTextViews() {
        mRecipeName.setText(mResults.recipeName);
        mTime.setText(" " + Integer.toString(mResults.totalTimeInSeconds / 60) + " " + mResources.getString(R.string.minutes));
        mRating.setRating(mResults.rating);
    }

    public void saveRecipe(PhraseResults recipe) {
        RecipeCursor cursor = mRecipeDao.readRecipesByYummlyUrl(recipe.id);
        if (cursor.moveToFirst() || cursor.getCount() != 0) {
            Toast.makeText(mContext, "Recipe already saved.", Toast.LENGTH_LONG).show();
            cursor.close();
            return;
        }
        cursor.close();

        long recipeId = mRecipeDao.insertRecipe(recipe.recipeName, recipe.id);

        for (String ingredient : recipe.ingredients) {
            if (mAdapter.getCheckedItems().contains(ingredient)) {
                mIngredientsDao.insertIngredient(ingredient, true, recipeId);
            } else {
                mIngredientsDao.insertIngredient(ingredient, false, recipeId);
            }
        }

        Toast.makeText(mContext, "Recipe and ingredients have been saved", Toast.LENGTH_SHORT).show();
        mCallback.updateDatabaseAndCloseDialog();
    }

    public interface DetailsListener {
        void startRecipeDetails(String recipeId);
        void updateDatabaseAndCloseDialog();
        void closeDialog();
    }
}
