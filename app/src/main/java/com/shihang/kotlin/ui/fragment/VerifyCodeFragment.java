package com.shihang.kotlin.ui.fragment;

import static com.mellivora.base.api.LoadingDialogApiKt.getDefaultLoadingDialogApi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.shihang.kotlin.R;
import com.shihang.kotlin.databinding.FragmentCodeDemoBinding;
import com.shihang.kotlin.vm.VerifyCodeVm;


public class VerifyCodeFragment extends Fragment {

    private VerifyCodeVm viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_code_demo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(VerifyCodeVm.class);
        viewModel.registerLoadingDialog(this, getDefaultLoadingDialogApi());
        FragmentCodeDemoBinding binding = FragmentCodeDemoBinding.bind(view);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setVm(viewModel);
    }
}
