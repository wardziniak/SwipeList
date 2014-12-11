package com.wardziniak.swipelist.swipe;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wardziniak on 12/8/14.
 * SwipeListAnimatorSet manage all existing animation. Used to cancel and start them
 */
public class SwipeListAnimatorSet extends DefaultAnimatorListener {

    private List<ObjectAnimator> listOfAnimations;

    private SwipeListView swipeListView;

    public SwipeListAnimatorSet(SwipeListView swipeListView) {
        listOfAnimations = new ArrayList<ObjectAnimator>();
        this.swipeListView = swipeListView;
    }

    public void startAnimations(List<ObjectAnimator> objectAnimators, final int position, final AnimationType animationType) {
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
    public void onAnimationEnd(Animator animation) {
        listOfAnimations.remove(animation);
    }
}
