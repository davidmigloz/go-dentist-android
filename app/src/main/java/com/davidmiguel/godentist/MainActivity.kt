package com.davidmiguel.godentist

import android.os.Bundle
import com.davidmiguel.godentist.core.base.BaseActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.DrawableRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.updateLayoutParams
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.davidmiguel.godentist.core.GoDentistNavGraphDirections
import com.davidmiguel.godentist.core.base.BaseFragment
import com.davidmiguel.godentist.core.R as RC
import com.davidmiguel.godentist.databinding.ActivityMainBinding
import com.google.android.material.bottomappbar.BottomAppBar


class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    private val rootFragments: Set<Int> = setOf(R.id.dashboard_fragment, R.id.clinics_fragment)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        navController = findNavController(R.id.navHostFragment)
        setupActionBar()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return menuInflater.inflate(R.menu.secondary_menu, menu).run { true }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.isNavigationMenuItem()) {
            return navController.navigate(
                GoDentistNavGraphDirections.actionOpenNavigationMenu(
                    navController.currentDestination?.id ?: -1
                )
            ).run { true }
        }
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    private fun setupActionBar() {
        appBarConfiguration = AppBarConfiguration(rootFragments)
        setSupportActionBar(binding.bottomAppBar)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (rootFragments.contains(destination.id)) {
                showMenuIcon()
            }
            hideFAB()
        }
    }

    /**
     * Shows root navigation menu icon in action bar.
     */
    private fun showMenuIcon() {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(RC.drawable.ic_menu_black_24dp)
        }
    }

    /**
     * Checks whether the menu item is the root navigation menu.
     */
    private fun MenuItem.isNavigationMenuItem(): Boolean {
        return itemId == android.R.id.home && rootFragments.contains(navController.currentDestination?.id)
    }

    public fun showFAB(
        @DrawableRes icon: Int,
        @BottomAppBar.FabAlignmentMode alignmentMode: Int = BottomAppBar.FAB_ALIGNMENT_MODE_END,
        onClickListener: (v: View) -> Unit
    ) {

        hideExtendedFAB()
        binding.fab.updateLayoutParams<CoordinatorLayout.LayoutParams> {
            anchorId = binding.bottomAppBar.id
        }
        binding.fab.setImageResource(icon)
        binding.bottomAppBar.fabAlignmentMode = alignmentMode
        binding.bottomAppBar.fabAnimationMode = BottomAppBar.FAB_ANIMATION_MODE_SCALE
        binding.fab.setOnClickListener(onClickListener)
        binding.fab.show()
        binding.fab.setOnClickListener {
            navController.navigate(R.id.add_clinic_fragment)
        }
    }

    public fun hideFAB() {
        if (binding.fab.visibility != View.GONE) {
            binding.fab.hide()
            binding.fab.setOnClickListener(null)
            binding.fab.updateLayoutParams<CoordinatorLayout.LayoutParams> {
                anchorId = View.NO_ID
            }
        }
    }

    public fun showExtendedFAB(
        @DrawableRes icon: Int,
        text: String,
        onClickListener: (v: View) -> Unit
    ) {
        hideFAB()
        binding.extendedFab.updateLayoutParams<CoordinatorLayout.LayoutParams> {
            anchorId = binding.bottomAppBar.id
        }
        binding.extendedFab.setIconResource(icon)
        binding.extendedFab.text = text
        binding.extendedFab.setOnClickListener(onClickListener)
        binding.extendedFab.show()
    }

    public fun hideExtendedFAB() {
        if (binding.extendedFab.visibility != View.GONE) {
            binding.extendedFab.hide()
            binding.extendedFab.setOnClickListener(null)
            binding.extendedFab.updateLayoutParams<CoordinatorLayout.LayoutParams> {
                anchorId = View.NO_ID
            }
        }
    }
}

fun BaseFragment.requireMainActivity(): MainActivity {
    return requireActivity() as? MainActivity
        ?: throw IllegalStateException("Fragment $this not attached to MainActivity.")
}
