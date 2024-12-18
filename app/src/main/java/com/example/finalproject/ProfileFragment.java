package com.example.finalproject;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import java.util.concurrent.Executor;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ProfileFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_VIDEO_CAPTURE = 2;
    private static final int REQUEST_CAMERA_PERMISSION = 3;
    private ImageView profileImageView;
    private VideoView profileVideoView;
    private String username;
    private ImageButton logout;

    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }


    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        profileImageView = view.findViewById(R.id.profileImageView);
        profileVideoView = view.findViewById(R.id.profileVideoView);
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMediaChoiceDialog();
            }
        });
        profileVideoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGalleryChoiceDialog();
            }
        });


        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userProfile", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", null); // mendaptkan data dari sharedpreference
        String email = sharedPreferences.getString("email", "email@gmail.com");
        TextView usernameTextView = view.findViewById(R.id.usernameProfile);
        TextView emailTextView = view.findViewById(R.id.emailProfile);

        usernameTextView.setText(username);
        emailTextView.setText(email);

        LinearLayout addProduct = view.findViewById(R.id.start_selling);
        addProduct.setOnClickListener(view12 -> {
            showBiometricPrompt(); // Trigger biometric authentication
        });


        logout = view.findViewById(R.id.logoutBtn);
        logout.setOnClickListener(view13 -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("username");
            editor.remove("email");
            editor.apply();
            Intent intent = new Intent(getContext(),MainActivity.class);
            startActivity(intent);

        });

        loadProfileMedia();
        return view;
    }

    private void showMediaChoiceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Pilih Media")
                .setItems(new CharSequence[]{"Ambil Gambar", "Pilih dari Galeri"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            // Check and request camera permission
                            checkCameraPermission();
                        } else {
                            showGalleryChoiceDialog();
                        }
                    }
                });
        builder.create().show();
    }

    private void showBiometricPrompt() {
        BiometricManager biometricManager = BiometricManager.from(getContext());

        if (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)
                == BiometricManager.BIOMETRIC_SUCCESS) {

            Executor executor = ContextCompat.getMainExecutor(getContext());

            BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor,
                    new BiometricPrompt.AuthenticationCallback() {
                        @Override
                        public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                            super.onAuthenticationSucceeded(result);
                            Toast.makeText(getContext(), "Authentication successful!", Toast.LENGTH_SHORT).show();
                            // Launch the AddProduct activity
                            Intent addIntent = new Intent(getContext(), AddProduct.class);
                            startActivity(addIntent);
                        }

                        @Override
                        public void onAuthenticationError(int errorCode, CharSequence errString) {
                            super.onAuthenticationError(errorCode, errString);
                            Toast.makeText(getContext(), "Authentication error: " + errString, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onAuthenticationFailed() {
                            super.onAuthenticationFailed();
                            Toast.makeText(getContext(), "Authentication failed. Try again!", Toast.LENGTH_SHORT).show();
                        }
                    });

            BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Fingerprint Authentication")
                    .setSubtitle("Place your finger on the sensor to continue")
                    .setNegativeButtonText("Cancel")
                    .build();

            biometricPrompt.authenticate(promptInfo);
        } else {
            Toast.makeText(getContext(), "Biometric authentication is not available on this device.",
                    Toast.LENGTH_SHORT).show();
        }
    }


    private void checkCameraPermission() {
        if (getActivity().checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            openCamera();
        }
    }

    private void openCamera() {
        if (getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        } else {
            Toast.makeText(getActivity(), "No camera found on this device", Toast.LENGTH_SHORT).show();
        }
    }

    private void showGalleryChoiceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Pilih dari Galeri")
                .setItems(new CharSequence[]{"Ambil Gambar", "Ambil Video"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                        } else {
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, REQUEST_VIDEO_CAPTURE);
                        }
                    }
                });
        builder.create().show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Uri imageUri = data.getData();
                if (imageUri == null && data.getExtras() != null) {
                    Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                    saveImageProfile(imageBitmap);
                    profileImageView.setImageBitmap(imageBitmap);
                    profileVideoView.setVisibility(View.GONE);
                    profileImageView.setVisibility(View.VISIBLE);

                } else if (imageUri != null) {
                    profileImageView.setImageURI(imageUri);
                    saveMediaUri(imageUri, "image");
                }
            } else if (requestCode == REQUEST_VIDEO_CAPTURE) {
                Uri videoUri = data.getData();

                if (videoUri != null) {
                    profileVideoView.setVideoURI(videoUri);
                    profileVideoView.start();
                    saveMediaUri(videoUri, "video");
                    profileImageView.setVisibility(View.GONE);
                    profileVideoView.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void saveImageProfile(Bitmap imageBitmap) {
        try {
            File file = new File(getActivity().getFilesDir(), username + "_profile_image.jpg");
            FileOutputStream fos = new FileOutputStream(file);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveMediaUri(Uri mediaUri, String type) {
        try {
            File file;
            if (type.equals("image")) {
                file = new File(getActivity().getFilesDir(), username + "_profile_image.jpg");
            } else {
                file = new File(getActivity().getFilesDir(), username + "_profile_video.mp4");
            }
            InputStream inputStream = getActivity().getContentResolver().openInputStream(mediaUri);
            FileOutputStream fos = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }
            fos.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadProfileMedia() {
        profileVideoView.setVisibility(View.GONE);

        File imageFile = new File(getActivity().getFilesDir(), username + "_profile_image.jpg");
        File videoFile = new File(getActivity().getFilesDir(), username + "_profile_video.mp4");

        if (imageFile.exists() && (!videoFile.exists() || imageFile.lastModified() > videoFile.lastModified())) {
            Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            profileImageView.setImageBitmap(bitmap);
            profileImageView.setVisibility(View.VISIBLE);
        } else if (videoFile.exists()) {
            profileVideoView.setVideoURI(Uri.fromFile(videoFile));
            profileVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    profileVideoView.start();
                }
            });
            profileVideoView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadProfileMedia();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                // Permission denied, show a message to the user
                Toast.makeText(getActivity(), "Camera permission is required to take pictures", Toast.LENGTH_SHORT).show();
            }
        }
    }
}