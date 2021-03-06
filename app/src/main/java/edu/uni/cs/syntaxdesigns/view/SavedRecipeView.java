package edu.uni.cs.syntaxdesigns.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import edu.uni.cs.syntaxdesigns.R;
import edu.uni.cs.syntaxdesigns.VOs.RecipeIdVo;
import edu.uni.cs.syntaxdesigns.VOs.SavedRecipeVo;

public class SavedRecipeView extends LinearLayout {

    private ImageView mCloseDialog;
    private RecipeIdVo mRecipe;
    private TextView mRecipeName;
    private ImageView mRecipeImage;
    private RatingsView mRating;
    private TextView mTime;
    private ListView mListView;
    private Button mViewDirections;
    private SavedRecipeVo mSavedRecipeVo;
    private SavedRecipeViewListener mListener;
    private CheckBox mFavorite;

    private Context mContext;

    public SavedRecipeView(Context context, RecipeIdVo recipe, SavedRecipeVo savedRecipeVo) {
        super(context);

        mRecipe = recipe;
        mSavedRecipeVo = savedRecipeVo;
        initialize(context);
    }

    public SavedRecipeView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initialize(context);
    }

    private void initialize(Context context) {
        LayoutInflater.from(context).inflate(R.layout.saved_recipe_dialog_view, this);

        mContext = context;

        mCloseDialog = (ImageView) findViewById(R.id.close_dialog);
        mRecipeName = (TextView) findViewById(R.id.recipe_name);
        mListView = (ListView) findViewById(R.id.ingredients_list);
        mTime = (TextView) findViewById(R.id.time_in_minutes);
        mRating = (RatingsView) findViewById(R.id.rating_by_stars);
        mViewDirections = (Button) findViewById(R.id.view_directions);
        mRecipeImage = (ImageView) findViewById(R.id.recipe_image);
        mFavorite= (CheckBox) findViewById(R.id.dialog_favorite);

        Picasso.with(context).load(mRecipe.images.get(0).hostedMediumUrl)
               .placeholder(R.drawable.ic_launcher)
               .error(R.drawable.ic_launcher)
               .fit()
               .centerCrop()
               .into(mRecipeImage);

        setTextViews();

        mCloseDialog.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.closeDialog();
            }
        });

        mFavorite.setChecked(mSavedRecipeVo.isFavorite);

        mFavorite.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mSavedRecipeVo.isFavorite = mFavorite.isChecked();
                mListener.updateFavorite(mSavedRecipeVo.rowId, mFavorite.isChecked());
            }
        });

        initializeListView();

        mViewDirections.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.startRecipeDetails();
            }
        });

    }

    public void setListener(SavedRecipeViewListener listener) {
        mListener = listener;
    }

    private void initializeListView() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, mRecipe.ingredientLines);
        mListView.setAdapter(adapter);
    }

    private void setTextViews() {
        mRecipeName.setText(mRecipe.name);
        mTime.setText(" " + mRecipe.totalTime);
        mRating.setRating(mRecipe.rating);
    }

    public interface SavedRecipeViewListener {
        void startRecipeDetails();
        void updateFavorite(long id, boolean isFavorite);
        void closeDialog();
    }
}
