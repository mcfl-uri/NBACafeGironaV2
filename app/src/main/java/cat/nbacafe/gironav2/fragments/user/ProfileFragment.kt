package cat.nbacafe.gironav2.fragments.user

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import cat.nbacafe.gironav2.R
import cat.nbacafe.gironav2.databinding.FragmentProfileBinding
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.File
import java.io.IOException
import java.util.*

class ProfileFragment : Fragment() {

    private lateinit var alias: String
    private lateinit var auth: FirebaseAuth
    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private var firebaseStorage: FirebaseStorage? = null
    private var storageReference: StorageReference? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentProfileBinding>(
            inflater,
            R.layout.fragment_profile, container, false
        )

        val oldPic = binding.profilePic.drawable

        auth = FirebaseAuth.getInstance()
        val db = Firebase.firestore
        firebaseStorage = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        binding.emailEditText.setText(auth.currentUser?.email.toString())
        db.collection("client").document(auth.currentUser?.email.toString()).get().addOnSuccessListener {
            if (it.exists() && it.get("username") != null)
                binding.usernameEditText.setText(it.get("username") as String)
        }

        alias = auth.currentUser?.email.toString()


        val storage = FirebaseStorage.getInstance().reference.child("$alias.jpg")
        val image = File.createTempFile("img", "jpg")
        storage.getFile(image).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(image.absolutePath)
            binding.profilePic.setImageBitmap(bitmap)
        }.addOnFailureListener {
            binding.profilePic.setImageResource(R.drawable.profile)
        }

        binding.changePassBtn.setOnClickListener { View ->
            binding.changePassBtn.visibility = android.view.View.GONE
            binding.passLayout.visibility = android.view.View.VISIBLE
        }

        binding.profilePic.setOnClickListener { View ->
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Escull la imatge"), PICK_IMAGE_REQUEST)
        }

        binding.saveUserBtn.setOnClickListener { View ->

            val username = binding.usernameEditText.text.toString()
            val email = binding.emailEditText.text.toString()

            if (binding.profilePic.drawable != oldPic)
                uploadImage()

            if (binding.passEditText.text.toString() != "" && binding.passEditText.text.toString().length >= 8) {
                auth.currentUser?.updatePassword(binding.passEditText.text.toString())
                    ?.addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(context, "Contrasenya canviada", Toast.LENGTH_LONG).show()
                        }
                    }
            }

            if (username != "") {
                db.collection("client").document(email).set(
                    hashMapOf(
                        "username" to username,
                        "appId" to alias
                    )
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(context, "Canvis realitzats", Toast.LENGTH_SHORT).show()
                        view?.findNavController()
                            ?.navigate(R.id.action_profileFragment_to_homeFragment)
                    } else {
                        Toast.makeText(context, "S'ha produït un error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.backBtn.setOnClickListener { View ->
            view?.findNavController()?.navigate(R.id.action_profileFragment_to_homeFragment)
        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if(data == null || data.data == null){
                return
            }

            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, filePath)
                val imatge = view?.findViewById<ImageView>(R.id.profilePic)
                imatge?.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun uploadImage(){
        if(filePath != null){
            val imageName = "$alias.jpg"
            val ref = storageReference?.child(imageName)
            val uploadTask = ref?.putFile(filePath!!)

        }else{
            Toast.makeText(context, "Cal seleccionar una imatge", Toast.LENGTH_SHORT).show()
        }
    }

}