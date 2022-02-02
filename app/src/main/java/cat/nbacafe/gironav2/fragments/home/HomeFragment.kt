package cat.nbacafe.gironav2.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import cat.nbacafe.gironav2.R
import cat.nbacafe.gironav2.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentHomeBinding>(
            inflater,
            R.layout.fragment_home, container, false
        )

        val auth = FirebaseAuth.getInstance()
        val db = Firebase.firestore

        db.collection("client").document(auth.currentUser?.email.toString()).get().addOnSuccessListener {
            if (it.exists() && it.get("username") != null)
                binding.homeText.setText("Hola de nou, ${it.get("username") as String}!")
        }

        binding.logoutBtn.setOnClickListener { View ->
            FirebaseAuth.getInstance().signOut()
            view?.findNavController()?.navigate(R.id.action_homeFragment_to_loginFragment)
        }

        binding.profileBtn.setOnClickListener { View ->
            view?.findNavController()?.navigate(R.id.action_homeFragment_to_profileFragment)
        }

        return binding.root
    }
}