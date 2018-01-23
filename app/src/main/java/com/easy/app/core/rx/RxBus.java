package com.easy.app.core.rx;


import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

public class RxBus {

    private ConcurrentHashMap<Object, List<Subject>> subjectMapper = new ConcurrentHashMap<>();

    private static RxBus instance;

    private RxBus() {
    }

    public static RxBus get() {
        if (instance == null) {
            synchronized (RxBus.class) {
                if (instance == null) {
                    instance = new RxBus();
                }
            }
        }
        return instance;
    }

    public <T> Observable<T> register(@NonNull Class<T> clazz) {
        return register(clazz.getSimpleName(),clazz);
    }

    @SuppressWarnings("unchecked")
    public <T> Observable<T> register(@NonNull Object tag, @NonNull Class<T> clazz) {
        List<Subject> subjects = subjectMapper.get(tag);
        if (subjects == null) {
            subjects = new ArrayList<>();
            subjectMapper.put(tag, subjects);
        }
        Subject<T, T> subject = PublishSubject.<T>create();
        subjects.add(subject);
        return subject;
    }

    @SuppressWarnings("unchecked")
    public void unregister(@NonNull Object tag, @NonNull Observable observable) {
        List<Subject> subjects = subjectMapper.get(tag);
        if (subjects != null) {
            subjects.remove((Subject) observable);
            if (subjects.isEmpty()) {
                subjectMapper.remove(tag);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void post(@NonNull Object obj) {
        post(obj.getClass().getSimpleName(), obj);
    }

    @SuppressWarnings("unchecked")
    public void post(@NonNull Object tag, @NonNull Object obj) {
        List<Subject> subjects = subjectMapper.get(tag);
        if (subjects != null && !subjects.isEmpty()) {
            for (Subject subject : subjects) {
                subject.onNext(obj);
            }
        }
    }

}
