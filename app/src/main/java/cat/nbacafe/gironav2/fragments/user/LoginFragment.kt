package cat.nbacafe.gironav2.fragments.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import cat.nbacafe.gironav2.R
import cat.nbacafe.gironav2.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentLoginBinding>(
            inflater,
            R.layout.fragment_login, container, false
        )

        auth = FirebaseAuth.getInstance()

        binding.registraBtn.setOnClickListener { View ->
            view?.findNavController()?.navigate(R.id.action_loginFragment_to_registerFragment)
        }

        return binding.root
    }
}