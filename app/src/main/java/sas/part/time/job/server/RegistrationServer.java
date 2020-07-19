package sas.part.time.job.server;

import sas.part.time.job.dialog.SasLogoDialog;
import sas.part.time.job.pojo.ResultMsg;
import sas.part.time.job.pojo.User;
import sas.part.time.job.userInterface.IRegistration;
import sas.part.time.job.utils.PartTimeMessage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static sas.part.time.job.utils.PartTimeUtils.IMAGES;
import static sas.part.time.job.utils.PartTimeUtils.USER;

@SuppressLint("SimpleDateFormat")
public class RegistrationServer {

    private SasLogoDialog cusomeDialog;
    private Activity mActivity;
    private IRegistration CallBack;
    private String EmailId;
    private String Password;
    private User user;
    private File file;
    private PartTimeMessage appMessage = PartTimeMessage.getSingleInstance();
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    public RegistrationServer(Activity activity, IRegistration callBack, String _emailId, String _password, User _user, File _file) {
        mActivity = activity;
        CallBack = callBack;
        EmailId = _emailId;
        Password = _password;
        user = _user;
        file = _file;
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void execute() {
        onPreExecute();
        doInBackground();
    }

    protected void onPreExecute() {
        cusomeDialog = new SasLogoDialog(mActivity);
        cusomeDialog.show();
    }

    protected void onPostExecute(ResultMsg data) {
        cusomeDialog.dismiss();
        CallBack.onRegistration(data);
    }

    protected ResultMsg doInBackground() {
        final ResultMsg data = new ResultMsg();
        firebaseAuth.createUserWithEmailAndPassword(EmailId, Password)
                .addOnCompleteListener(mActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            updateUserData(task.getResult().getUser(), data);
                            updatePic(task.getResult().getUser(), data);
                        } else if (task.getException() != null) {
                            data.setResult(false);
                            data.setMessage(task.getException().getMessage());
                            onPostExecute(data);
                        }
                    }
                })
                .addOnFailureListener(mActivity, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        data.setResult(false);
                        data.setMessage(e.getMessage());
                        onPostExecute(data);
                    }
                });
        return data;
    }

    private void updateUserData(FirebaseUser currentUser, final ResultMsg data) {
        if (currentUser != null) {
            DatabaseReference childReference = databaseReference.child(USER).child(currentUser.getUid());
            childReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    data.setResult(true);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    data.setResult(false);
                    data.setMessage(error.getMessage());
                    onPostExecute(data);
                }
            });
            childReference.setValue(user);
        }
    }

    private void updatePic(final FirebaseUser currentUser, final ResultMsg data) {
        if (file != null) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(IMAGES).child(file.getName());
            UploadTask uploadTask = storageReference.putFile(Uri.fromFile(file));
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    StorageReference imageRef = FirebaseStorage.getInstance().getReference().child(IMAGES).child(file.getName());
                    Task<Uri> downloadUrl = imageRef.getDownloadUrl();
                    downloadUrl.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageReference = uri.toString();
                            user.setImageReference(imageReference);
                            Map<String, Object> map = new HashMap<>();
                            map.put("imageReference", imageReference);
                            databaseReference.child(USER).child(currentUser.getUid()).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    data.setResult(true);
                                    onPostExecute(data);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    data.setResult(false);
                                    data.setMessage(e.getMessage());
                                    onPostExecute(data);
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            data.setResult(false);
                            data.setMessage(e.getMessage());
                            onPostExecute(data);
                        }
                    });
                }
            });
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    data.setResult(false);
                    data.setMessage(e.getMessage());
                    onPostExecute(data);
                }
            });
        } else {
            onPostExecute(data);
        }
    }
}
