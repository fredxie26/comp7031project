package com.example.comp7031project;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class UserAdapter extends BaseAdapter {
    private Context context;
    private List<User> users;
    private DatabaseHelper databaseHelper;

    public UserAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
        this.databaseHelper = new DatabaseHelper(context);
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return users.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false);
        }

        User user = users.get(position);

        TextView textName = convertView.findViewById(R.id.text_name);
        ImageView imageStar = convertView.findViewById(R.id.image_star);
        TextView textAddress = convertView.findViewById(R.id.text_address);
        ImageView imageProfile = convertView.findViewById(R.id.image_profile);
        Spinner statusSpinner = convertView.findViewById(R.id.status_spinner);
        ImageButton buttonDelete = convertView.findViewById(R.id.button_delete);

        textName.setText("Name: " + user.getFirstName() + " " + user.getLastName());
        textAddress.setText("Address: " + user.getAddress());

        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(context,
                R.array.status_options, android.R.layout.simple_spinner_item);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(statusAdapter);

        // Set the default status in the spinner
        statusSpinner.setSelection(getStatusPosition(user.getStatus()));

        // On status change in the spinner, update database
        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View view, int position, long id) {
                String selectedStatus = (String) parentView.getItemAtPosition(position);
                user.setStatus(selectedStatus);
                databaseHelper.updateUserStatus(user.getId(), selectedStatus);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });

        // Load user photo or default image
        if (user.getPhotoPath() != null) {
            imageProfile.setImageURI(Uri.parse(user.getPhotoPath()));
        } else {
            imageProfile.setImageResource(R.drawable.cat);
        }

        if (user.isFocused()) {
            imageStar.setImageResource(R.drawable.star_yellow);
        } else {
            imageStar.setImageResource(R.drawable.star_grey);
        }

        // Toggle the focus status and update the database
        imageStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean newFocusStatus = !user.isFocused();
                user.setFocused(newFocusStatus);
                databaseHelper.updateFocusStatus(user.getId(), newFocusStatus);

                if (newFocusStatus) {
                    imageStar.setImageResource(R.drawable.star_yellow);
                    Toast.makeText(context, "User focused", Toast.LENGTH_SHORT).show();
                } else {
                    imageStar.setImageResource(R.drawable.star_grey);
                    Toast.makeText(context, "User unfocused", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Delete the user from the database
                boolean isDeleted = databaseHelper.deleteUser(user.getId());

                if (isDeleted) {
                    Toast.makeText(context, "User deleted", Toast.LENGTH_SHORT).show();

                    // Remove the user from the list and update the adapter
                    users.remove(position);
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(context, "Error deleting user", Toast.LENGTH_SHORT).show();
                }
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailViewActivity.class);
                intent.putExtra("firstName", user.getFirstName());
                intent.putExtra("lastName", user.getLastName());
                intent.putExtra("address", user.getAddress());
                intent.putExtra("photoPath", user.getPhotoPath());
                intent.putExtra("status", user.getStatus());
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    private int getStatusPosition(String status) {
        switch (status) {
            case "complete":
                return 1;
            case "start":
                return 2;
            default:
                return 0; // in_progress is the default
        }
    }
}
