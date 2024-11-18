package com.example.comp7031project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private ListView userListView;
    private UserAdapter userAdapter;
    private Button buttonFilter;
    private EditText searchEditText;
    private Button searchButton;
    private RadioGroup sortRadioGroup;

    private List<User> originalUserList;
    private List<User> filteredUserList;

    private boolean isFilterFocused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);

        buttonFilter = findViewById(R.id.button_filter);
        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);
        userListView = findViewById(R.id.user_list_view);
        sortRadioGroup = findViewById(R.id.sortRadioGroup);

        originalUserList = new ArrayList<>();
        filteredUserList = new ArrayList<>();

        Button createProfileButton = findViewById(R.id.button_create_profile);

        // Load initial data in the background
        new Thread(new Runnable() {
            @Override
            public void run() {
                originalUserList = databaseHelper.getAllUsers();
                filteredUserList.addAll(originalUserList);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Initialize the adapter after loading the users
                        userAdapter = new UserAdapter(MainActivity.this, filteredUserList);
                        userListView.setAdapter(userAdapter);
                    }
                });
            }
        }).start();

        // Search button functionality
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterList();
            }
        });

        // Sorting functionality
        sortRadioGroup.setOnCheckedChangeListener((group, checkedId) -> sortPersonList(checkedId));

        // Create profile button
        createProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateProfileActivity.class);
                startActivityForResult(intent, 1);  // Use startActivityForResult to get data back
            }
        });

        // Filter focused users
        buttonFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFilterFocused = !isFilterFocused;
                loadUsers();  // Refresh the list based on filter state
                buttonFilter.setText(isFilterFocused ? "Unfilter Focused" : "Filter Focused");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUsers();  // Refresh the list when returning to the MainActivity
    }

    private void filterList() {
        String query = searchEditText.getText().toString().toLowerCase();
        filteredUserList.clear();

        if (query != null && !query.isEmpty()) {
            for (User user : originalUserList) {
                if (user.getFullInfo().toLowerCase().contains(query)) {
                    filteredUserList.add(user);
                }
            }
        } else {
            filteredUserList.addAll(originalUserList);
        }

        // Notify the adapter of the changes
        if (userAdapter != null) {
            userAdapter.notifyDataSetChanged();
        }
    }

    private void sortPersonList(int checkedId) {
        Comparator<User> comparator = null;

        if (checkedId == R.id.sortByFirstName) {
            comparator = Comparator.comparing(User::getFirstName);
        } else if (checkedId == R.id.sortByLastName) {
            comparator = Comparator.comparing(User::getLastName);
        } else if (checkedId == R.id.sortByAddress) {
            comparator = Comparator.comparing(user -> getUnitNum(user.getAddress()));
        }

        if (comparator != null) {
            Collections.sort(filteredUserList, comparator);
            if (userAdapter != null) {
                userAdapter.notifyDataSetChanged();
            }
        }
    }

    public String getUnitNum(String input) {
        String[] parts = input.split("\\s+");
        return parts.length > 0 ? parts[0] : "";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            loadUsers();  // Reload users after profile creation
        }
    }

    private void loadUsers() {
        // Clear the current lists before reloading
        originalUserList.clear();
        filteredUserList.clear();

        // Load users based on filter state
        if (isFilterFocused) {
            List<User> focusedUsers = databaseHelper.getFocusedUsers();
//            Log.d("MainActivity", "Focused Users: " + focusedUsers.size());
            originalUserList.addAll(focusedUsers);
        } else {
            List<User> allUsers = databaseHelper.getAllUsers();
//            Log.d("MainActivity", "All Users: " + allUsers.size());
            originalUserList.addAll(allUsers);
        }

        filteredUserList.addAll(originalUserList);

        // Update the adapter if it's initialized
        if (userAdapter == null) {
            userAdapter = new UserAdapter(this, filteredUserList);
            userListView.setAdapter(userAdapter);
        } else {
            userAdapter.notifyDataSetChanged();
        }
    }

}
