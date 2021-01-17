package com.example.vnuepttest176;

import android.app.Application;
import android.util.LruCache;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.common.model.RemoteModelManager;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.TranslateRemoteModel;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class TranslateViewModel extends AndroidViewModel {
    private static final int NUM_TRANSLATORS = 3;

    private final RemoteModelManager modelManager;

    private final LruCache<TranslatorOptions, Translator> translator =
            new LruCache<TranslatorOptions, Translator>(NUM_TRANSLATORS){
                @Override
                protected Translator create(TranslatorOptions key) {
                    return Translation.getClient(key);
                }

                @Override
                protected void entryRemoved(boolean evicted, TranslatorOptions key, Translator oldValue, Translator newValue) {
                    oldValue.close();
                }
            };
    MutableLiveData<Language> sourceLang = new MutableLiveData<>();
    MutableLiveData<Language> targetLang = new MutableLiveData<>();
    MutableLiveData<String> sourceText = new MutableLiveData<>();
    MediatorLiveData<ResultOrError> translatedText = new MediatorLiveData<>();
    MutableLiveData<List<String>> availableModels = new MutableLiveData<>();

    public TranslateViewModel(@NonNull Application application){
        super(application);
        modelManager = RemoteModelManager.getInstance();

        final OnCompleteListener<String> processTranslation = new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(task.isSuccessful()){
                    translatedText.setValue(new ResultOrError(task.getResult(),null));
                }
                else{
                    translatedText.setValue(new ResultOrError(null,task.getException()));
                }

                fetchDownloadModels();
            }
        };
        translatedText.addSource(sourceText, new Observer<String>() {
            @Override
            public void onChanged(@NonNull String s) {
                translate().addOnCompleteListener(processTranslation);
            }
        });

        Observer<Language> languageObserver = new Observer<Language>() {
            @Override
            public void onChanged(@NonNull Language language) {
                translate().addOnCompleteListener(processTranslation);
            }
        };

        translatedText.addSource(sourceLang,languageObserver);
        translatedText.addSource(targetLang,languageObserver);

        fetchDownloadModels();
    }

    List<Language> getAvailableLanguages(){
        List<Language> languages = new ArrayList<>();
        List<String> languageIds = TranslateLanguage.getAllLanguages();
        for(String languageId: languageIds){
            languages.add(
                    new Language(TranslateLanguage.fromLanguageTag(languageId))
            );

        }
        return languages;
    }

    private TranslateRemoteModel getModel(String languageCode){
        return new TranslateRemoteModel.Builder(languageCode).build();
    }

    void downloadLanguage(Language language){
        TranslateRemoteModel model = getModel(TranslateLanguage.fromLanguageTag(language.getCode()));
        modelManager.download (model,new DownloadConditions.Builder ().build ())
                .addOnCompleteListener (new OnCompleteListener<Void> ( ) {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        fetchDownloadModels ();
                    }
                });
    }
    void deleteLanguage(Language language){
        TranslateRemoteModel model = getModel(TranslateLanguage.fromLanguageTag(language.getCode()));
        modelManager.deleteDownloadedModel(model).addOnCompleteListener(
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        fetchDownloadModels();
                    }
                });
    }

    public Task<String> translate(){
        final String text = sourceText.getValue();
        final Language source = (Language) sourceLang.getValue();
        final Language target = (Language) targetLang.getValue();
        if(source == null || target == null || text == null || text.isEmpty()){
            return Tasks.forResult("");
        }
        String sourceLangCode = TranslateLanguage.fromLanguageTag(source.getCode());
        String targetLangCode = TranslateLanguage.fromLanguageTag(target.getCode());
        //Create a Translator object, configuring it with the source and target languages:
        TranslatorOptions options = new TranslatorOptions.Builder()
                .setSourceLanguage(sourceLangCode).setTargetLanguage(targetLangCode).build();

        return translator.get(options).downloadModelIfNeeded().continueWithTask(
                new Continuation<Void, Task<String>>() {
                    @Override
                    public Task<String> then(@NonNull Task<Void> task) throws Exception {
                        if(task.isSuccessful()){
                            return translator.get(options).translate(text);
                        }
                        else{
                            Exception e = task.getException();
                            if(e == null){
                                e = new Exception(getApplication().getString(R.string.common_google_play_services_unknown_issue));
                            }
                            return Tasks.forException(e);
                        }
                    }
                }
        );
    }

    private void fetchDownloadModels(){
        // Get translation models stored on the device.
        modelManager.getDownloadedModels(TranslateRemoteModel.class).addOnSuccessListener(
                new OnSuccessListener<Set<TranslateRemoteModel>>() {
                    @Override
                    public void onSuccess(Set<TranslateRemoteModel> translateRemoteModels) {
                        List<String> modelCodes = new ArrayList<>(translateRemoteModels.size());
                        for(TranslateRemoteModel model: translateRemoteModels){
                            modelCodes.add(model.getLanguage());
                        }
                        Collections.sort(modelCodes);
                        availableModels.setValue(modelCodes);
                    }
                }
        );

    }

    static class ResultOrError{
        final @Nullable String result;
        final @Nullable Exception error;

        ResultOrError(@Nullable String result, @Nullable Exception error){
            this.result = result;
            this.error = error;
        }
    }

    static class Language implements Comparable<Language>{
        private String code;
        Language (String code){
            this.code = code;
        }
        String getDisplayName(){
            return new Locale(code).getDisplayName();
        }
        String getCode(){
            return code;
        }

        public boolean equals(Object o){
            if(o == this){
                return true;
            }
            if(!(o instanceof Language)){
                return false;
            }
            Language otherLang = (Language) o;
            return otherLang.code.equals(code);
        }

        @NonNull
        public String toString(){
            return code + " - "+getDisplayName();
        }

        @Override
        public int hashCode() {
            return code.hashCode();
        }

        @Override
        public int compareTo(@NonNull Language o) {
            return this.getDisplayName().compareTo(o.getDisplayName());
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        translator.evictAll();
    }
}
