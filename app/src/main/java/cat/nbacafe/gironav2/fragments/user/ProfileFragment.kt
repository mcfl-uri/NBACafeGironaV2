package cat.nbacafe.gironav2.fragments.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import cat.nbacafe.gironav2.R
import cat.nbacafe.gironav2.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentProfileBinding>(
            inflater,
            R.layout.fragment_profile, container, false
        )

        auth = FirebaseAuth.getInstance()
        val db = Firebase.firestore

        binding.emailEditText.setText(auth.currentUser?.email.toString())
        db.collection("client").document(auth.currentUser?.email.toString()).get().addOnSuccessListener {
            binding.usernameEditText.setText(it.get("username") as String)
        }

        binding.changePassBtn.setOnClickListener { View ->
            binding.changePassBtn.visibility = android.view.View.GONE
            binding.passLayout.visibility = android.view.View.VISIBLE
        }

        binding.saveUserBtn.setOnClickListener { View ->

            val username = binding.usernameEditText.text.toString()
            val email = binding.emailEditText.text.toString()

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
                        "username" to username
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

}