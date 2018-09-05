package com.info.inet.pm;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.info.inet.pm.R;

import java.util.Objects;

public class driver_tab extends Fragment {

    ProgressDialog pd;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    RecyclerView driverRecycler;
    private FirebaseRecyclerAdapter<driverDetail,DriverHolder> driverAdapter;

    GoogleSignInAccount account;
    String UserId;

    private View driverView;
    Query query;
    String AdminId = "103829564535792869003";
    int AdminFlag = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        driverView=  inflater.inflate(R.layout.driver_tab,container,false);

        driverRecycler = driverView.findViewById(R.id.driverRecyclerView);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Driver");

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        driverRecycler.setHasFixedSize(false);
        driverRecycler.setLayoutManager(layoutManager);

        account = GoogleSignIn.getLastSignedInAccount(getContext());
        if(account!= null){
            UserId = account.getId();

            if(Objects.equals(UserId, AdminId)){
                query = databaseReference.orderByKey();
                AdminFlag = 1;
            }
            else
            {
                query = databaseReference.orderByChild("reqStatus").equalTo("Approved");
                AdminFlag = 0;
            }
        }


        return driverView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {



        pd = new ProgressDialog(getContext());
        pd.setMessage("Please wait");
        pd.setCancelable(true);
        pd.show();
        super.onActivityCreated(savedInstanceState);

        // Admin id == "103829564535792869003"





        FirebaseRecyclerOptions firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<driverDetail>().setQuery(query, driverDetail.class).build();

        driverAdapter = new FirebaseRecyclerAdapter<driverDetail, DriverHolder>(firebaseRecyclerOptions) {


            @NonNull
            @Override
            public DriverHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.driver_card, parent, false);
                return new DriverHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull DriverHolder holder, int position, @NonNull final driverDetail model) {

                holder.SetDriverData(model.driverName, model.driverLicense, model.driverPhone, model.driverCity, model.driverImage, model.reqStatus);

                if(model.reqStatus.equals("Approved")){
                    holder.driverStatus.setTextColor(getResources().getColor(R.color.Approved));
                }

                final String key = getRef(position).getKey();
                if(AdminFlag == 0){
                    holder.driverStatus.setVisibility(View.GONE);
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(),bookingActivity.class);
                            intent.putExtra("Key",key);
                            startActivity(intent);
                        }
                    });
                }
                else {
                    holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {

                            final String[] menuItems = getResources().getStringArray(R.array.menuItems);
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setItems(menuItems, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    switch (which){
                                        case 1:
                                            databaseReference = FirebaseDatabase.getInstance().getReference("Driver/"+ key);
                                            databaseReference.removeValue();
                                            FirebaseStorage storage = FirebaseStorage.getInstance();
                                            StorageReference storageReference = storage.getReferenceFromUrl(model.driverImage);
                                            storageReference.delete();
                                            Toast.makeText(getContext(), "Post Deleted Successfully", Toast.LENGTH_SHORT).show();
                                            break;

                                        case 0:
                                            databaseReference = FirebaseDatabase.getInstance().getReference("Driver/"+ key+"/reqStatus");
                                            databaseReference.setValue("Approved");
                                            break;
                                    }


                                }
                            });
                            builder.show();

                            return true;
                        }
                    });
                }

                /*
                if(model.reqStatus.equals("Approved")){
                    holder.driverStatus.setBackgroundColor(getResources().getColor(R.color.Approved));
                }
                */

/*
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(AdminFlag == 0){

                        }
                        else{

                            Intent intent = new Intent(getActivity(),adminActivity.class);
                            intent.putExtra("Key",key);
                            intent.putExtra("From","Driver");
                            startActivity(intent);
                        }

                    }
                });

                */

            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                if(pd.isShowing())
                    pd.dismiss();
            }
        };

        driverRecycler.setAdapter(driverAdapter);

    }


/*
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if(v.getId()== R.id.driverRecyclerView){
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            String[] menuItems = getResources().getStringArray(R.array.menuItems);
            for(int i = 0; i< menuItems.length;i++){

                menu.add(Menu.NONE,i,i,menuItems[i]);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int menuItemIndex = item.getItemId();



        return true;
    }

*/

    @Override
    public void onStart() {
        super.onStart();

        if(!isNetworkAvailable()){
            //Toast.makeText(CategoryActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext() );
            builder.setMessage("Turn on mobile data or wifi and retry")
                    .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            onStart();
                        }
                    })
                    .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getActivity().finish();
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.setTitle("No internet Connection");
            alertDialog.show();
        }
        else
            driverAdapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        driverAdapter.stopListening();
    }

    public static class DriverHolder extends RecyclerView.ViewHolder{

        TextView driverName, driverLicense, driverAdhaar, driverPhone, driverCity, driverStatus;
        ImageView driverImage;

        public DriverHolder(View itemView){
            super(itemView);
            driverName = itemView.findViewById(R.id.card_driverName);
            driverLicense = itemView.findViewById(R.id.card_driverLicense);
            //driverAdhaar = itemView.findViewById(R.id.card_driverAdhaar);
            driverPhone = itemView.findViewById(R.id.card_driverNumber);
            driverCity = itemView.findViewById(R.id.card_driverCity);
            driverImage = itemView.findViewById(R.id.driverImage);
            driverStatus = itemView.findViewById(R.id.driverStatus);

        }

        public void SetDriverData( String driver_Name, String driver_License, String driver_Phone, String driver_City, String driver_Image, String driver_status){

            driverName.setText(driver_Name);
            driverPhone.setText(driver_Phone);
            driverCity.setText(driver_City);
            driverLicense.setText(driver_License);
            driverStatus.setText(driver_status);

            Glide.with(itemView.getContext())
                    .load(driver_Image)
                    .apply(new RequestOptions()
                        .placeholder(R.color.colorAccent))
                    .thumbnail(0.1f)
                    .into(driverImage);
        }
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
