package com.newtry.instagramproject.Model;

import java.util.ArrayList;

//// storyModel is used no more
//public class StoryModel {
//    int story,storyType,profile;
//    String name;
//
//    public int getStory() {
//        return story;
//    }
//
//    public void setStory(int story) {
//        this.story = story;
//    }
//
//    public int getStoryType() {
//        return storyType;
//    }
//
//    public void setStoryType(int storyType) {
//        this.storyType = storyType;
//    }
//
//    public int getProfile() {
//        return profile;
//    }
//
//    public void setProfile(int profile) {
//        this.profile = profile;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public StoryModel(int story, int storyType, int profile, String name) {
//        this.story = story;
//        this.storyType = storyType;
//        this.profile = profile;
//        this.name = name;
//    }
//}
public class Story
{
    private String storyBy;
    private long StoryAt;
    ArrayList<UserStories> stories;

    public Story() {
    }

    public String getStoryBy() {
        return storyBy;
    }

    public void setStoryBy(String storyBy) {
        this.storyBy = storyBy;
    }

    public long getStoryAt() {
        return StoryAt;
    }

    public void setStoryAt(long storyAt) {
        StoryAt = storyAt;
    }

    public ArrayList<UserStories> getStories() {
        return stories;
    }

    public void setStories(ArrayList<UserStories> stories) {
        this.stories = stories;
    }
}