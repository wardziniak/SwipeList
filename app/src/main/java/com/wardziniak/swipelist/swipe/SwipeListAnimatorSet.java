package com.wardziniak.swipelist.swipe;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wardziniak on 12/8/14.
 */
public class SwipeListAnimatorSet implements Animator.AnimatorListener {

    private List<ObjectAnimator> listOfAnimations;

    public SwipeListAnimatorSet() {
        listOfAnimations = new ArrayList<ObjectAnimator>();
    }

    public void startAnimation(ObjectAnimator objectAnimator) {
        listOfAnimations.add(objectAnimator);
        objectAnimator.addListener(this);
        objectAnimator.start();
    }

    public void startAnimations(List<ObjectAnimator> objectAnimators) {
        listOfAnimations.addAll(objectAnimators);
        List<Animator> animators = new ArrayList<Animator>();
        for (ObjectAnimator objectAnimator: objectAnimators) {
            objectAnimator.addListener(this);
            animators.add(objectAnimator);
        }
        final AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animators);
        animatorSet.start();
    }

    public boolean containsViewAnimation(ItemSwipeListView itemSwipeListView) {
        for (ObjectAnimator objectAnimator: listOfAnimations) {
            if (objectAnimator.getTarget().equals(itemSwipeListView.getFrontView()))
                return true;
        }
        return false;
    }

    private List<ObjectAnimator> getItemSwipeListViewAnimations(ItemSwipeListView itemSwipeListView) {
        List<ObjectAnimator> objectAnimators = new ArrayList<ObjectAnimator>();
        for (ObjectAnimator objectAnimator: listOfAnimations) {
            if (((View) objectAnimator.getTarget()).getParent().equals(itemSwipeListView))
                objectAnimators.add(objectAnimator);
        }
        return objectAnimators;
    }

    public void cancelItemSwipeListViewAnimations(ItemSwipeListView ItemSwipeListView) {
        List<ObjectAnimator> objectAnimators = getItemSwipeListViewAnimations(ItemSwipeListView);
        for (ObjectAnimator objectAnimator: objectAnimators) {
            objectAnimator.cancel();
        }
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        listOfAnimations.remove(animation);
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}
