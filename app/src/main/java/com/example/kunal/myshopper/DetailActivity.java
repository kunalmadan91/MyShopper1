package com.example.kunal.myshopper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.List;

import Adapter.ProductCartAdapter;
import butterknife.Bind;
import butterknife.ButterKnife;
import fragment.MyCart;
import model.Global;
import model.Products;
import model.SharedPrefrencesProduct;

import static com.example.kunal.myshopper.R.id.fab_menu;

public class DetailActivity extends AppCompatActivity {
    SharedPrefrencesProduct prefrencesMovie;
     Products  prodct;

    @Bind(R.id.name)
    TextView prodName;

    @Bind(R.id.cost)
    TextView price;

    @Bind(R.id.prod_description)
    TextView description;

    @Bind(R.id.backdrop)
    ImageView mainImage;


    Button btncart;
    SharedPreferences sharedpreferences;

    @Bind(fab_menu)
                                            FloatingActionMenu fabMenu;
    @Bind(R.id.fab_to_read)
    FloatingActionButton fabToRead;
    @Bind(R.id.fab_reading)             FloatingActionButton fabReading;


    @Bind(R.id.nested)
    NestedScrollView bookDetailHolder;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       /* getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.cart);*/

        prefrencesMovie = new SharedPrefrencesProduct();
        Intent intent = getIntent();

        prodct = intent.getExtras().getParcelable("DATA");

        initCollapsingToolbar();
        prodName.setText(prodct.getName());

       price.setText(String.valueOf(prodct.getPrice()));



        int id = prodct.getImageId();

        Glide.with(DetailActivity.this).load(id).into(mainImage);
       // mainImage.setImageDrawable(id);

        description.setText(prodct.getDescription());

        if (checkFavouriteItem(prodct)) {
            fabToRead.setLabelText("remove from cart");
        } else {
            fabToRead.setLabelText("add to cart");

        }


        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bookDetailHolder.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (oldScrollY < scrollY) {
                    fabMenu.hideMenuButton(true);
                } else {
                    fabMenu.showMenuButton(true);
                }
            }
        });




        fabToRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(fabToRead.getLabelText().equalsIgnoreCase("Add to Cart")){

                    prefrencesMovie.addFavorite(DetailActivity.this, prodct);
                /*btn.setText("REMOVE FROM CART");
                btn.setTag("red");*/

                    fabToRead.setLabelText("remove from cart");

                    Global.amt = Global.amt + prodct.getPrice();

                    sharedpreferences = getSharedPreferences("MYPREF", Context.MODE_PRIVATE);

                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("amt",String.valueOf(Global.amt));
                    editor.commit();

                    ArrayList<Products> product = prefrencesMovie.getFavorites(DetailActivity.this);

                    Toast.makeText(DetailActivity.this, "Product added to cart Successfully!!", Toast.LENGTH_SHORT).show();

                    if(MyCart.adapter != null) {
                        // Toast.makeText(DetailActivity.this, "pls add sometihg to cart", Toast.LENGTH_SHORT).show();
                        MyCart.adapter = new
                                ProductCartAdapter(DetailActivity.this, product);

                        MyCart.recyclerView.
                                setAdapter(MyCart.adapter);

                    }
                }
                else if(fabToRead.getLabelText().equalsIgnoreCase("remove from cart")) {

                    prefrencesMovie.removeFromFavourites(DetailActivity.this, prodct);

                    fabToRead.setLabelText("add to cart");
                    Toast.makeText(DetailActivity.this, "Product removed from cart Successfully!!", Toast.LENGTH_SHORT).show();
                    ArrayList<Products> product = prefrencesMovie.getFavorites(DetailActivity.this);

                    Global.amt = Global.amt - prodct.getPrice();

                    sharedpreferences = getSharedPreferences("MYPREF", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("amt",String.valueOf(Global.amt));
                    editor.commit();
                    if(MyCart.adapter != null) {
                        MyCart.adapter = new
                                ProductCartAdapter(DetailActivity.this, product);

                        MyCart.recyclerView.
                                setAdapter(MyCart.adapter);
                    }
                }


            }
        });

        fabReading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(DetailActivity.this,MainActivity.class);

                intent1.putExtra("FLAG","CART");
                startActivity(intent1);
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_cart) {
            Intent intent1 = new Intent(DetailActivity.this,MainActivity.class);

            intent1.putExtra("FLAG","CART");
            startActivity(intent1);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(prodct.getName());
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }


    public boolean checkFavouriteItem(Products prodct) {
        boolean check = false;
        List<Products> favourites = prefrencesMovie.getFavorites(DetailActivity.this);

        if (favourites != null) {
            for (Products movies1 : favourites) {
                if (movies1.getProductId() == prodct.getProductId()) {
                    check = true;
                    break;
                }
            }
        }
        return check;
    }

}
