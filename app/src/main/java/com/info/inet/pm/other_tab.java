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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

public class other_tab extends Fragment {

    ProgressDialog pd;

    FirebaseDatabase database;
    DatabaseReference databaseReference;
    RecyclerView otherRecycler;
    private FirebaseRecyclerAdapter<otherDetail,OtherHolder> otherAdapter;

    GoogleSignInAccount account;
    String UserId;

    Query query;
    String AdminId = "103829564535792869003";
    int AdminFlag = 0;

    private View otherView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        otherView =  inflater.inflate(R.layout.other_tab,container,false);




        otherRecycler = otherView.findViewById(R.id.otherTabRecyclerView);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Other");

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        otherRecycler.setHasFixedSize(false);
        otherRecycler.setLayoutManager(layoutManager);

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



        return otherView;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        pd = new ProgressDialog(getContext());
        pd.setMessage("Please wait");
        pd.setCancelable(true);
        pd.show();


        //Query query = databaseReference.orderByKey();

        FirebaseRecyclerOptions firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<otherDetail>().setQuery(query, otherDetail.class).build();

        otherAdapter = new FirebaseRecyclerAdapter<otherDetail, OtherHolder>(firebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull OtherHolder holder, int position, @NonNull final otherDetail model) {
                holder.setOtherData(model.otherVehicleType,model.otherVehicleLicensePlateNo,model.tourType,model.otherVehicleImage,model.reqStatus);

                if(model.reqStatus.equals("Approved")){
                    holder.otherStatus.setTextColor(getResources().getColor(R.color.Approved));
                }


                final String key = getRef(position).getKey();
                if(AdminFlag == 0){
                    holder.otherStatus.setVisibility(View.GONE);
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                Intent intent = new Intent(getActivity(),bookingActivity.class);
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
                                            databaseReference = FirebaseDatabase.getInstance().getReference("Other/"+ key);
                                            databaseReference.removeValue();
                                            FirebaseStorage storage = FirebaseStorage.getInstance();
                                            StorageReference storageReference = storage.getReferenceFromUrl(model.otherVehicleImage);
                                            storageReference.delete();
                                            Toast.makeText(getContext(), "Post Deleted Successfully", Toast.LENGTH_SHORT).show();
                                            break;

                                        case 0:
                                            databaseReference = FirebaseDatabase.getInstance().getReference("Other/"+ key+"/reqStatus");
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

            }

            @NonNull
            @Override
            public OtherHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.other_card, parent, false);
                return new OtherHolder(view);
            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                if(pd.isShowing())
                    pd.dismiss();
            }
        };

        otherRecycler.setAdapter(otherAdapter);
    }

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
            otherAdapter.startListening();


    }

    @Override
    public void onStop() {
        super.onStop();
        otherAdapter.stopListening();
    }

    public static class OtherHolder extends RecyclerView.ViewHolder{

        TextView vehicleType, vehicleLicense,TourType, otherStatus;
        ImageView vehicleImage;

        public OtherHolder(View itemView) {
            super(itemView);

            vehicleType = itemView.findViewById(R.id.card_otherVehicleType);
            vehicleLicense = itemView.findViewById(R.id.card_otherVehicleLicesnse);
            TourType = itemView.findViewById(R.id.card_tourType);
            vehicleImage = itemView.findViewById(R.id.card_otherImage);
            otherStatus = itemView.findViewById(R.id.otherReqStatus);
        }

        public void setOtherData (String vehicle_type, String vehicle_lic, String vehicle_tourType, String vehicle_image, String vehicle_status){
            vehicleType.setText(vehicle_type);
            vehicleLicense.setText(vehicle_lic);
            TourType.setText(vehicle_tourType);
            otherStatus.setText(vehicle_status);

            Glide.with(itemView.getContext())
                    .load(vehicle_image)
                    .apply(new RequestOptions()
                        .placeholder(R.color.colorAccent))
                        .thumbnail(0.1f)
                        .into(vehicleImage);
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
