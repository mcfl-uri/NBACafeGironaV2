package cat.nbacafe.gironav2.fragments.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import cat.nbacafe.gironav2.R
import cat.nbacafe.gironav2.databinding.FragmentLoginBinding
import cat.nbacafe.gironav2.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class RegisterFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentRegisterBinding>(
            inflater,
            R.layout.fragment_register, container, false
        )

        auth = FirebaseAuth.getInstance()

        binding.createUserBtn.setOnClickListener { View ->
            auth.createUserWithEmailAndPassword(binding.emailEditText.text.toString(), binding.passEditText.text.toString())
        }

        return binding.root
    }
}