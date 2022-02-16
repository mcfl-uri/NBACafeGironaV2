package cat.nbacafe.gironav2.fragments.addreview

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import cat.nbacafe.gironav2.R
import cat.nbacafe.gironav2.common.SharedViewModel
import cat.nbacafe.gironav2.databinding.FragmentNewReviewBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class NewReviewFragment : Fragment() {

    val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentNewReviewBinding>(
            inflater,
            R.layout.fragment_new_review, container, false
        )

        val item = sharedViewModel.getItem()

        val storage = FirebaseStorage.getInstance().reference.child(item.nomImatge + ".jpg")
        val imatge = File.createTempFile("img", "jpg")
        storage.getFile(imatge).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(imatge.absolutePath)
            binding.topImage.setImageBitmap(bitmap)
        }.addOnFailureListener {
            binding.topImage.setImageResource(R.drawable.basketball_court)
        }

        auth = FirebaseAuth.getInstance()
        val db = Firebase.firestore

        binding.courseNameView.setText(item.nom)
        binding.coursePriceView.setText("${item.preu} €")

        val alias = auth.currentUser?.email.toString()

        val stored = FirebaseStorage.getInstance().reference.child("$alias.jpg")
        val image = File.createTempFile("img", "jpg")
        stored.getFile(image).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(image.absolutePath)
            binding.profilePic.setImageBitmap(bitmap)
        }.addOnFailureListener {
            binding.profilePic.setImageResource(R.drawable.person_outline_black)
        }

        binding.sendBtn.setOnClickListener { View ->
            db.collection("review").document(alias+item.nom).set(
                hashMapOf(
                    "producte" to item.nom,
                    "usuari" to alias,
                    "text" to binding.editTextTextMultiLine.text.toString()
                )
            ).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(context, "Review publicada", Toast.LENGTH_SHORT).show()
                    view?.findNavController()?.navigate(R.id.action_newReviewFragment_to_coursesFragment)
                } else {
                    Toast.makeText(context, "S'ha produït un error", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.cancelBtn.setOnClickListener { View ->
            view?.findNavController()?.navigate(R.id.action_newReviewFragment_to_coursesFragment)
        }

        return binding.root
    }

}