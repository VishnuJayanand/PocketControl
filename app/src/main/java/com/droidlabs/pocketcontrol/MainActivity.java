package com.droidlabs.pocketcontrol;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.widget.GridView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Main activity of the app.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Method to create an app instance.
     */
    private GridView gridView;
    private ListView listView;

    /**
     * methood to create a category list.
     * @param savedInstanceState bundle
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_listview);

        //Create Cartegory objects
        Category health = new Category(R.drawable.health, "Health");
        Category transport = new Category(R.drawable.transport, "Transport");
        Category shopping = new Category(R.drawable.shopping, "Shopping");
        Category food = new Category(R.drawable.food, "Food");
        Category study = new Category(R.drawable.study, "Study");
        Category rent = new Category(R.drawable.rent, "Rent");

        //Add the Cartegory object to cartegoriesList
        ArrayList<Category> cartegoriesList = new ArrayList<>();
        cartegoriesList.add(health);
        cartegoriesList.add(transport);
        cartegoriesList.add(shopping);
        cartegoriesList.add(food);
        cartegoriesList.add(study);
        cartegoriesList.add(rent);

        //Create Transaction objects
        Date today = Calendar.getInstance().getTime();
        Transaction transactionA = new Transaction(1, "Pay rent", today, "expense", -300, "nothing", "euro", rent.getImageId(), rent.getTitle());
        Transaction transactionB = new Transaction(2, "Pay food", today, "expense", -120, "nothing", "euro", food.getImageId(), food.getTitle());
        Transaction transactionC = new Transaction(3, "Pay for clothes", today, "income", 100, "nothing", "euro", shopping.getImageId(), shopping.getTitle());
        Transaction transactionD = new Transaction(4, "Recieve money for study", today, "income", 90, "nothing", "euro", study.getImageId(), study.getTitle());

        ArrayList<Transaction> transactionsList = new ArrayList<>();
        transactionsList.add(transactionA);
        transactionsList.add(transactionB);
        transactionsList.add(transactionC);
        transactionsList.add(transactionD);

        //Set the adapter for Cartegory adapter
        CategoryAdapter adapter = new CategoryAdapter(this, R.layout.category_griditem, cartegoriesList);
        //Set the adapter for Transaction adapter
        TransactionSubAdapter transactionSubAdapter = new TransactionSubAdapter(this, R.layout.transaction_listitem, transactionsList);


        //Set List View
        listView = findViewById(R.id.transactionListView);
        //Set Grid View for category
//        gridView = findViewById(R.id.categoryGridView);

        listView.setAdapter(transactionSubAdapter);
        //Register item lick
//        gridView.setOnItemClickListener(onItemClick);
//        gridView.setOnTouchListener(new View.OnTouchListener() {
//
//            @Override
//            public boolean onTouch(final View v, final MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_MOVE) {
//                    return true;
//                }
//                return false;
//            }
//
//        });
//
//    }
//    AdapterView.OnItemClickListener onItemClick = new AdapterView.OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            //Do any thing when user click to item
//            Toast.makeText(MainActivity.this, "select category", Toast.LENGTH_SHORT).show();
//        }
//    };
    }
}
