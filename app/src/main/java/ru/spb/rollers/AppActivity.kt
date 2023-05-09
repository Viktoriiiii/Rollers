package ru.spb.rollers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import ru.spb.rollers.databinding.ActivityAppBinding

class AppActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAppBinding
    lateinit var navController: NavController

//    private val currentFragment: Fragment
//        get() = supportFragmentManager.findFragmentById(R.id.fragmentContainer)!!
//
//    private val fragmentListener = object : FragmentManager.FragmentLifecycleCallbacks() {
//        override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
//            super.onFragmentViewCreated(fm, f, v, savedInstanceState)
//            updateUi()
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = Navigation.findNavController(this,
            R.id.fragmentContainer)

        MAIN = this;


//        if (savedInstanceState == null) {
//            supportFragmentManager
//                .beginTransaction()
//                .add(R.id.fragmentContainer, Authorization())
//                .commit()
//        }
//
//        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentListener, false)
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentListener)
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        // we've called setSupportActionBar in onCreate,
//        // that's why we need to override this method too
//        updateUi()
//        return true
//    }

//    override fun onSupportNavigateUp(): Boolean {
//        onBackPressed()
//        return true
//    }
//
//    override fun showAuthorizationScreen() {
//        launchFragment(Authorization())
//    }
//
//    override fun showRegistrationScreen() {
//        TODO("Not yet implemented")
//    }
//
//    override fun showHomePageScreen() {
//        TODO("Not yet implemented")
//    }
//
//    override fun showMyProfileScreen() {
//        TODO("Not yet implemented")
//    }
//
//    override fun showListMyContactsScreen() {
//        TODO("Not yet implemented")
//    }
//
//    override fun showListMyEventsScreen() {
//        TODO("Not yet implemented")
//    }
//
//    override fun showMyRoutesScreen() {
//    }
//
//    override fun goBack() {
//        onBackPressed()
//    }

//    override fun goToMenu() {
//        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
//    }
//
//    override fun <T : Parcelable> publishResult(result: T) {
//        supportFragmentManager.setFragmentResult(result.javaClass.name, bundleOf(KEY_RESULT to result))
//    }
//
//    override fun <T : Parcelable> listenResult(
//        clazz: Class<T>,
//        owner: LifecycleOwner,
//        listener: ResultListener<T>
//    ) {
//        supportFragmentManager.setFragmentResultListener(clazz.name, owner, FragmentResultListener
//        { key, bundle ->
//            listener.invoke(bundle.getParcelable(KEY_RESULT)!!)
//    })
//    }
//
//    private fun launchFragment(fragment: Fragment) {
//        supportFragmentManager
//            .beginTransaction()
////            .setCustomAnimations(
////                R.anim.slide_in,
////                R.anim.fade_out,
////                R.anim.fade_in,
////                R.anim.slide_out
////            )
//            .addToBackStack(null)
//            .replace(R.id.fragmentContainer, fragment)
//            .commit()
//    }
//
//    private fun updateUi() {
//        val fragment = currentFragment
//
//        if (fragment is HasCustomTitle) {
//            binding.toolbar.title = getString(fragment.getTitleRes())
//        } else {
//            binding.toolbar.title = getString(R.string)
//        }
//
//        if (supportFragmentManager.backStackEntryCount > 0) {
//            supportActionBar?.setDisplayHomeAsUpEnabled(true)
//            supportActionBar?.setDisplayShowHomeEnabled(true)
//        } else {
//            supportActionBar?.setDisplayHomeAsUpEnabled(false)
//            supportActionBar?.setDisplayShowHomeEnabled(false)
//        }
//
//        if (fragment is HasCustomAction) {
//            createCustomToolbarAction(fragment.getCustomAction())
//        } else {
//            binding.toolbar.menu.clear()
//        }
//    }
//
//    private fun createCustomToolbarAction(action: CustomAction) {
//        binding.toolbar.menu.clear() // clearing old action if it exists before assigning a new one
//
//        val iconDrawable = DrawableCompat.wrap(ContextCompat.getDrawable(this, action.iconRes)!!)
//        iconDrawable.setTint(Color.WHITE)
//
//        val menuItem = binding.toolbar.menu.add(action.textRes)
//        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
//        menuItem.icon = iconDrawable
//        menuItem.setOnMenuItemClickListener {
//            action.onCustomAction.run()
//            return@setOnMenuItemClickListener true
//        }
//    }
//
//    companion object {
//        @JvmStatic private val KEY_RESULT = "RESULT"
//    }
//}

}