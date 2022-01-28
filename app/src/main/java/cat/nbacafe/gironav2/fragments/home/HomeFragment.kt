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

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentHomeBinding>(
            inflater,
            R.layout.fragment_home, container, false
        )

        binding.logoutBtn.setOnClickListener { View ->
            FirebaseAuth.getInstance().signOut()
            view?.findNavController()?.navigate(R.id.action_homeFragment_to_loginFragment)
        }

        return binding.root
    }
}