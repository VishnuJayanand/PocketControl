package com.droidlabs.pocketcontrol.ui.home;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.droidlabs.pocketcontrol.R;
import com.droidlabs.pocketcontrol.db.account.Account;
import com.droidlabs.pocketcontrol.db.budget.Budget;
import com.droidlabs.pocketcontrol.db.category.Category;
import com.droidlabs.pocketcontrol.db.chartdata.TotalExpenditurePerCategory;
import com.droidlabs.pocketcontrol.db.chartdata.TotalExpenditurePerDay;
import com.droidlabs.pocketcontrol.db.chartdata.TotalIncomePerDay;
import com.droidlabs.pocketcontrol.db.defaults.Defaults;
import com.droidlabs.pocketcontrol.db.transaction.Transaction;
import com.droidlabs.pocketcontrol.ui.budget.BudgetViewModel;
import com.droidlabs.pocketcontrol.ui.categories.CategoryViewModel;
import com.droidlabs.pocketcontrol.ui.settings.DefaultsViewModel;
import com.droidlabs.pocketcontrol.ui.signin.UserViewModel;
import com.droidlabs.pocketcontrol.ui.transaction.TransactionViewModel;
import com.droidlabs.pocketcontrol.utils.CurrencyUtils;
import com.droidlabs.pocketcontrol.utils.DateUtils;
import com.droidlabs.pocketcontrol.utils.SharedPreferencesUtils;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class HomeFragment extends Fragment {

    private Animation topAnimation;
    private TextView textViewAmount, textViewNetBalance, selectedAccountTitle;
    private TextView accountIncomeText, accountExpenseText, accountBalanceText;
    private CardView selectedAccountColor;
    private List<Account> accountList;
    private Button addAccountButton;
    private UserViewModel userViewModel;
    private AccountViewModel accountViewModel;
    private String[] accountNames;
    private MaterialAlertDialogBuilder dialogBuilder;
    private SharedPreferencesUtils sharedPreferencesUtils;
    private TransactionViewModel transactionViewModel;
    private CategoryViewModel categoryViewModel;
    private DefaultsViewModel defaultsViewModel;
    private BudgetViewModel budgetViewModel;
    private String stringCurrency;
    private LinearLayout summaryContainer, infoTipContainer;
    private RecyclerView featuredRecycler, infoTipRecycler;
    private RecyclerView.Adapter adapter, infoAdapter;

    private LinearLayout barChartContainer;
    private HorizontalBarChart barChartCategoryExpenditure;

    private LinearLayout lineChartContainer;
    private LineChart lineChartIncomeExpenditure;

    @Nullable
    @Override
    public final View onCreateView(
            final LayoutInflater inf, final @Nullable ViewGroup container, final @Nullable Bundle savedInstanceState) {
        View view = inf.inflate(R.layout.fragment_home, container, false);

        defaultsViewModel = new ViewModelProvider(this).get(DefaultsViewModel.class);
        budgetViewModel = new ViewModelProvider(this).get(BudgetViewModel.class);

        barChartContainer = view.findViewById(R.id.chartContainer);
        barChartCategoryExpenditure = view.findViewById(R.id.barChartCategoryExpenditure);

        lineChartContainer = view.findViewById(R.id.lineChartContainer);
        lineChartIncomeExpenditure = view.findViewById(R.id.lineChartIncomeExpenditure);

        LinearLayout accountsWrapper = view.findViewById(R.id.accountsWrapper);
        Button switchAccount = accountsWrapper.findViewById(R.id.switchAccountButton);
        selectedAccountColor = view.findViewById(R.id.selectedAccountColor);

        sharedPreferencesUtils = new SharedPreferencesUtils(getActivity().getApplication());
        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        selectedAccountTitle = view.findViewById(R.id.selectedAccountTitle);
        textViewAmount = view.findViewById(R.id.homeScreenTop);
        textViewNetBalance = view.findViewById(R.id.homeScreenNetBalanceText);

        addAccountButton = view.findViewById(R.id.addAccountButton);
        accountIncomeText = view.findViewById(R.id.accountIncomeText);
        accountExpenseText = view.findViewById(R.id.accountExpenseText);
        accountBalanceText = view.findViewById(R.id.accountBalanceText);

        topAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.top_animation);
        textViewAmount.setAnimation(topAnimation);
        textViewNetBalance.setAnimation(topAnimation);

        summaryContainer = view.findViewById(R.id.summaryContainer);
        infoTipContainer = view.findViewById(R.id.home_infoTips);

        String stringCurrencyCode = defaultsViewModel.getDefaultValue("Currency");
        stringCurrency = defaultsViewModel.getCurrencySymbol(stringCurrencyCode);

        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        accountViewModel.getAccounts().observe(getViewLifecycleOwner(), new Observer<List<Account>>() {
            @Override
            public void onChanged(final List<Account> accounts) {
                accountNames = new String[accounts.size()];

                for (int i = 0; i < accounts.size(); i++) {
                    accountNames[i] = accounts.get(i).getName();
                }

                buildSelectAccountDialog(accountNames);
            }
        });

        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        transactionViewModel.setCategoryFilter(false, "-1");
        calculateAccountBalances();

        addAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Fragment fragment = new AddAccountFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        switchAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                dialogBuilder.show();
            }
        });

        updateSelectedAccountInformation();

        //Hooks
        featuredRecycler = view.findViewById(R.id.featured_recycler);
        infoTipRecycler = view.findViewById(R.id.infoTips_recycler);

        featuredRecycler(view);
        infoTipRecycler(view);

        initializeBarChartCategoryExpenditure();
        initializeLineChartExpenditureIncomes();

        return view;
    }

    /**
     * Populate card view.
     * @param rView view
     */
    private void featuredRecycler(final View rView) {

        featuredRecycler.setHasFixedSize(true);
        featuredRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        ArrayList<FeaturedHelperClass> featuredLocation = new ArrayList<>();

        //Top Spent Transaction
        Float transactionExpenseId = 0f;
        transactionExpenseId = transactionViewModel.getTransactionIdByHighestExpenseAmount();

        if (transactionExpenseId != 0) {
            Transaction transaction = transactionViewModel.getTransactionById(transactionExpenseId.longValue());
            Category category = categoryViewModel.getSingleCategory(Integer.parseInt(transaction.getCategory()));

            String amount = CurrencyUtils.formatAmount(transaction.getAmount(), stringCurrency);
            String categoryName = category.getName();
            int icon = category.getIcon();

            summaryContainer.setVisibility(View.VISIBLE);
            featuredLocation.add(new FeaturedHelperClass("Top Spent Transaction", categoryName, amount, icon));

        }

        //Top spend Category
        Float sumTotal = 0f;
        String name = "";
        int iconc = 0;
        List<Category> listCat = categoryViewModel.getAllCategories();
        if (listCat != null) {
            for (Category cat : listCat) {
                if (!cat.getName().equalsIgnoreCase("Income")) {
                    if (sumTotal <= transactionViewModel.getTotalIAmountByCategoryId(cat.getId().toString())) {
                        sumTotal = transactionViewModel.getTotalIAmountByCategoryId(cat.getId().toString());
                        name = cat.getName();
                        iconc = cat.getIcon();
                    }
                }
            }
            if (sumTotal != 0) {
                summaryContainer.setVisibility(View.VISIBLE);
                String amount = CurrencyUtils.formatAmount(sumTotal, stringCurrency);
                featuredLocation.add(new FeaturedHelperClass("Top Spent Category", name, amount, iconc));
            }

        }

        //Highest Income
        Float transactionIncomeId = 0f;
        transactionIncomeId = transactionViewModel.getTransactionIdByHighestIncomeAmount();

        if (transactionIncomeId != 0) {
            Transaction transaction = transactionViewModel.getTransactionById(transactionIncomeId.longValue());

            String amount = CurrencyUtils.formatAmount(transaction.getAmount(), stringCurrency);
            String categoryName = "Income";
            int icon = R.drawable.category_icons_saving;

            summaryContainer.setVisibility(View.VISIBLE);
            featuredLocation.add(new FeaturedHelperClass("Highest Income", categoryName, amount, icon));

        }

        adapter = new FeaturedAdapter(featuredLocation);

        featuredRecycler.setAdapter(adapter);
    }

    /**
     * Populate card view.
     * @param rView view
     */
    private void infoTipRecycler(final View rView) {

        infoTipRecycler.setHasFixedSize(true);
        infoTipRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        ArrayList<InfoTipHelperClass> infoTipLocation = new ArrayList<>();

        List<Transaction> listTransaction = transactionViewModel.getTransactionsForExport();
        List<Budget> listBudget = budgetViewModel.getBudgetsForExport();
        List<Category> listCategory = categoryViewModel.getCategoriesForExport();
        List<Defaults> listDefaults = defaultsViewModel.getAllDefaults();

        if (listBudget.isEmpty()) {
            infoTipContainer.setVisibility(rView.VISIBLE);
            infoTipLocation.add(new InfoTipHelperClass("Budget", "Try setting a budget to monitor your expenses"));
        } else {
            if (!listTransaction.isEmpty()) {

                for (Category category : listCategory) {
                    String catId = category.getId().toString();
                    Budget budget = budgetViewModel.getBudgetForCategory(catId);
                    String catName = category.getName();

                    if (budget != null) {
                        Float budgetAmount = budget.getMaxAmount();
                        Float totalAmount = transactionViewModel.getTotalIAmountByCategoryId(catId);

                        if (budgetAmount - totalAmount < 0) {
                            infoTipContainer.setVisibility(rView.VISIBLE);
                            infoTipLocation.add(new InfoTipHelperClass("Budget Exceeded",
                                    "Budget limit exceeded for  " + catName));
                        } else if (budgetAmount - totalAmount == 0) {
                            infoTipContainer.setVisibility(rView.VISIBLE);
                            infoTipLocation.add(new InfoTipHelperClass("Budget Full",
                                    "Budget limit is full for " + catName));
                        } else if (budgetAmount - totalAmount <= 50) {
                            infoTipContainer.setVisibility(rView.VISIBLE);
                            infoTipLocation.add(new InfoTipHelperClass("Low Budget",
                                    "Monitor your expenses for " + catName));
                        }
                    }
                }
            }
        }

        ArrayList<String> defaultCategoryList = new ArrayList<String>();
        ArrayList<String> defaultCategoryListObtained = new ArrayList<String>();
        defaultCategoryList.add("Health");
        defaultCategoryList.add("Food");
        defaultCategoryList.add("Transport");
        defaultCategoryList.add("Shopping");
        defaultCategoryList.add("Study");
        defaultCategoryList.add("Rent");
        defaultCategoryList.add("Income");

        for (Category category : listCategory) {
            String catName = category.getName();
            defaultCategoryListObtained.add(catName);

        }

        if (defaultCategoryList.equals(defaultCategoryListObtained)) {
            infoTipContainer.setVisibility(rView.VISIBLE);
            infoTipLocation.add(new InfoTipHelperClass("Custom Category",
                    "Try setting defaults in settings screen to ease transaction addition"));
        }

        String defaultCurrency = defaultsViewModel.getDefaultValue("Currency");
        String defaultPaymentMode = defaultsViewModel.getDefaultValue("Payment Mode");
        String defaultCategory = defaultsViewModel.getDefaultValue("Category");

        if (defaultCurrency.equals("EUR") && defaultPaymentMode.equals("Cash") && defaultCategory.equals("Health")) {
            infoTipContainer.setVisibility(rView.VISIBLE);
            infoTipLocation.add(new InfoTipHelperClass("Set Defaults",
                    "Try setting defaults in settings screen to ease transaction addition"));
        } else if (defaultCurrency.equals("EUR")) {
            infoTipContainer.setVisibility(rView.VISIBLE);
            infoTipLocation.add(new InfoTipHelperClass("Set Default Currency",
                    "Try setting defaults in settings screen to ease transaction addition"));
        } else if (defaultPaymentMode.equals("Cash")) {
            infoTipContainer.setVisibility(rView.VISIBLE);
            infoTipLocation.add(new InfoTipHelperClass("Set Default Payment Mode",
                    "Try setting defaults in settings screen to ease transaction addition"));
        } else if (defaultCategory.equals("Health")) {
            infoTipContainer.setVisibility(rView.VISIBLE);
            infoTipLocation.add(new InfoTipHelperClass("Set Default Category",
                    "Try setting defaults in settings screen to ease transaction addition"));
        }


        infoAdapter = new InfoTipAdapter(infoTipLocation);
        infoTipRecycler.setAdapter(infoAdapter);

    }

    /**
     * Calculate balances.
     */
    private void calculateAccountBalances() {
        float accountIncome, accountExpense, accountBalance, totalBalance;

        accountIncome = transactionViewModel.getTotalIncomeByAccountId();
        accountExpense = transactionViewModel.getTotalExpenseByAccountId();
        accountBalance = accountIncome - accountExpense;
        totalBalance = transactionViewModel.getTotalIncomeByUserId() - transactionViewModel.getTotalExpenseByUserId();

        accountIncomeText.setText(CurrencyUtils.formatAmount(accountIncome, stringCurrency));
        accountExpenseText.setText(CurrencyUtils.formatAmount(accountExpense, stringCurrency));
        accountBalanceText.setText(CurrencyUtils.formatAmount(accountBalance, stringCurrency));

        textViewAmount.setText(CurrencyUtils.formatAmount(totalBalance, stringCurrency));
    }

    /**
     * Populate account dialog.
     * @param names account names.
     */
    private void buildSelectAccountDialog(final String[] names) {
        dialogBuilder = new MaterialAlertDialogBuilder(getContext())
                .setTitle("Select your account:")
                .setItems(names, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        Account selectedAccount = accountViewModel.getAccountByName(names[which]);

                        userViewModel.updateUserSelectedAccount(String.valueOf(selectedAccount.getId()));
                        sharedPreferencesUtils.setCurrentAccountId(String.valueOf(selectedAccount.getId()));

                        updateSelectedAccountInformation();
                        calculateAccountBalances();

                        Fragment fragment = new HomeFragment();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container, fragment);
                        fragmentTransaction.commit();
                    }
                });
    }

    /**
     * Update selected account info.
     */
    private void updateSelectedAccountInformation() {
        Account selectedAcc = accountViewModel
                .getAccountById(Integer.parseInt(sharedPreferencesUtils.getCurrentAccountIdKey()));

        String accountColor = selectedAcc.getColor();

        selectedAccountTitle.setText(selectedAcc.getName());

        if (accountColor != null) {
            selectedAccountColor.setCardBackgroundColor(Color.parseColor(accountColor));
        } else {
            selectedAccountColor.setCardBackgroundColor(getResources().getColor(R.color.projectColorDefault, null));
        }

    }

    /**
     * Initialize bar chart.
     */
    private void initializeBarChartCategoryExpenditure() {
        List<BarEntry> barChartData = new ArrayList<>();
        List<TotalExpenditurePerCategory> listTotalExpenditurePerCategory = transactionViewModel
                .getTotalExpenditurePerCategory();

        if (listTotalExpenditurePerCategory.size() == 0) {
            barChartContainer.setVisibility(View.GONE);
            return;
        } else {
            barChartContainer.setVisibility(View.VISIBLE);
        }

        HashMap<Integer, TotalExpenditurePerCategory> parsedCategories = parseCategoryAmounts(
                listTotalExpenditurePerCategory
        );

        for (HashMap.Entry<Integer, TotalExpenditurePerCategory> entry : parsedCategories.entrySet()) {
            barChartData.add(new BarEntry(entry.getKey(), entry.getValue().getCategoryAmount()));
        }

        BarDataSet barDataSet = new BarDataSet(barChartData, "");
        barDataSet.setForm(Legend.LegendForm.NONE);
        barDataSet.setColor(getContext().getColor(R.color.colorExpense));

        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.9f);
        barData.setValueTextSize(12f);
        barData.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(final float value) {
                String stringCurrencyCode = defaultsViewModel.getDefaultValue("Currency");
                String currency = defaultsViewModel.getCurrencySymbol(stringCurrencyCode);
                return CurrencyUtils.formatAmount(value, currency, value > 999999);
            }
        });

        //to hide right Y and top X border
        YAxis rightYAxis = barChartCategoryExpenditure.getAxisRight();
        rightYAxis.setAxisMinimum(0f);
        rightYAxis.setAxisMaximum(barData.getYMax() * 1.5f);
        rightYAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(final float value) {
                String stringCurrencyCode = defaultsViewModel.getDefaultValue("Currency");
                String currency = defaultsViewModel.getCurrencySymbol(stringCurrencyCode);
                return CurrencyUtils.formatAmount(value, currency, value > 999999);
            }
        });

        YAxis leftYAxis = barChartCategoryExpenditure.getAxisLeft();
        leftYAxis.setEnabled(false);
        leftYAxis.setDrawGridLines(false);
        leftYAxis.setAxisMinimum(0f);
        leftYAxis.setAxisMaximum(barData.getYMax() * 1.5f);

        XAxis xAxis = barChartCategoryExpenditure.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);

        // X-Axis styling
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(final float value) {
                TotalExpenditurePerCategory totalExpenditure = parsedCategories.get((int) value);

                if (totalExpenditure != null) {
                    String categoryIndex = String.valueOf(totalExpenditure.getCategoryId());
                    return StringUtils
                            .abbreviate(
                                    categoryViewModel.getSingleCategory(
                                            Integer.parseInt(categoryIndex)
                                    ).getName(),
                                    15
                            );
                }

                return "";
            }
        });

        if (parsedCategories.size() * 100 < 250) {
            barChartCategoryExpenditure.setMinimumHeight(250);
        } else {
            barChartCategoryExpenditure.setMinimumHeight(parsedCategories.size() * 100);
        }

        barChartCategoryExpenditure.setData(barData);
        barChartCategoryExpenditure.getDescription().setEnabled(false);
        barChartCategoryExpenditure.setNoDataText("No data available! Add transactions to visualize the chart");
        barChartCategoryExpenditure.invalidate();
    }

    /**
     * HashMap category amount parser.
     * @param expenditures list of expenditures.
     * @return hashmap with continuous integer keys.
     */
    private HashMap<Integer, TotalExpenditurePerCategory> parseCategoryAmounts(
            final List<TotalExpenditurePerCategory> expenditures
    ) {
        HashMap<Integer, TotalExpenditurePerCategory> finalHashMap = new HashMap<>();
        int counter = 0;

        for (TotalExpenditurePerCategory expenditure : expenditures) {
            finalHashMap.put(counter, expenditure);
            counter++;
        }

        return finalHashMap;
    }

    /**
     * Initialize line chart.
     */
    private void initializeLineChartExpenditureIncomes() {
        Calendar endDate = DateUtils.getStartOfCurrentDay();
        Calendar startDate = DateUtils.getStartOfCurrentDay();

        startDate.add(Calendar.DAY_OF_YEAR, -30);

        List<TotalExpenditurePerDay> listTotalExpenditurePerDay = transactionViewModel
                .getTotalExpenditurePerDay(startDate.getTimeInMillis(), endDate.getTimeInMillis());
        List<TotalIncomePerDay> listTotalIncomePerDay = transactionViewModel
                .getTotalIncomePerDay(startDate.getTimeInMillis(), endDate.getTimeInMillis());

        HashMap<Long, Float> expenditureHashMap = new HashMap<>();
        HashMap<Long, Float> incomeHashMap = new HashMap<>();

        for (TotalExpenditurePerDay expenditurePerDay : listTotalExpenditurePerDay) {
            expenditureHashMap.put(expenditurePerDay.getDate(), expenditurePerDay.getTotalExpenditure());
        }

        for (TotalIncomePerDay incomePerDay : listTotalIncomePerDay) {
            incomeHashMap.put(incomePerDay.getDate(), incomePerDay.getTotalIncome());
        }

        List<Entry> lineDataTotalIncome = new ArrayList<>();
        List<Entry> lineDataTotalExpenditure = new ArrayList<>();

        float sumTotalIncome = 0;
        float sumTotalExpenditure = 0;
        Calendar currentDate = startDate;

        for (int i = 30; i >= 0; i--) {
            Float expenditurePerDay = expenditureHashMap.get(currentDate.getTimeInMillis());
            Float incomePerDay = incomeHashMap.get(currentDate.getTimeInMillis());

            if (expenditurePerDay != null) {
                sumTotalExpenditure += expenditurePerDay;
            }

            if (incomePerDay != null) {
                sumTotalIncome += incomePerDay;
            }

            lineDataTotalExpenditure.add(new Entry(currentDate.getTimeInMillis(), sumTotalExpenditure));
            lineDataTotalIncome.add(new Entry(currentDate.getTimeInMillis(), sumTotalIncome));

            currentDate.add(Calendar.DAY_OF_YEAR, 1);
        }

        LineDataSet incomeDataSet = new LineDataSet(lineDataTotalIncome, "Cumulative income");
        LineDataSet expenditureDataSet = new LineDataSet(lineDataTotalExpenditure, "Cumulative expenditure");

        incomeDataSet.setDrawValues(false);
        incomeDataSet.setColor(getContext().getColor(R.color.colorIncome));
        incomeDataSet.setCircleColor(getContext().getColor(R.color.colorIncome));

        expenditureDataSet.setDrawValues(false);
        expenditureDataSet.setColor(getContext().getColor(R.color.colorExpense));
        expenditureDataSet.setCircleColor(getContext().getColor(R.color.colorExpense));

        LineData lineData = new LineData(incomeDataSet, expenditureDataSet);

        YAxis rightYAxis = lineChartIncomeExpenditure.getAxisRight();
        rightYAxis.setEnabled(false);
        rightYAxis.setDrawGridLines(false);
        rightYAxis.setAxisMinimum(0);

        YAxis leftYAxis = lineChartIncomeExpenditure.getAxisLeft();
        leftYAxis.setAxisMinimum(0);
        leftYAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(final float value) {
                String stringCurrencyCode = defaultsViewModel.getDefaultValue("Currency");
                String currency = defaultsViewModel.getCurrencySymbol(stringCurrencyCode);
                return CurrencyUtils.formatAmount(value, currency, value > 999999);
            }
        });

        XAxis xAxis = lineChartIncomeExpenditure.getXAxis();

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(final float value) {
                return DateUtils.formatDate((long) value, "dd-MM");
            }
        });

        lineChartIncomeExpenditure.setMinimumHeight(600);
        lineChartIncomeExpenditure.setData(lineData);
        lineChartIncomeExpenditure.getDescription().setEnabled(false);
        lineChartIncomeExpenditure.invalidate();
    }
}
