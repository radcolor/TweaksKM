# Contributing to project

Hey! We are delighted to see your interest in helping and contributing us on Tweaks app.

Let's get started with setting up the development enviornment for work and app to run. Please read all the steps below carefully.

## Setting up enviroment

### Step 1: Setting up Git

First you need to install [Git](https://git-scm.com/). after that you need to fork our repository located at https://github.com/theradcolor/tweaks.

<p align="center"><img src="https://raw.githubusercontent.com/theradcolor/Tweaks/master/assets/cont_img_frk.png"></p>

After forking the repository, clone the forked repo to the system using

```
git clone https://github.com/<YOUR_GITHUB_USERNAME>/tweaks.git
```
Change directory to the newly downloaded forked copy.

```
cd tweaks
```
### Step 2: Configuring Android Studio and SDKs

- Download [Android Studio and SDKs](https://developer.android.com/studio).
- Change directory to ~/android-studio/bin/ and run studio bash (For Unix/Linux bases system).
- Further docs and instructions can be found on Androids developer site.

### Step 3: Git Branching

We should always work in branches to keep the master undisturbed and enforce a good collaboration enviornment.

Before creating a new branch ALWAYS perform the below steps to ensure that when you branch off from master you have the latest upstream version.

To make a branch, do:

```
git branch <BRANCH_NAME>
```
Then switch to newly made branch by doing:
```
git checkout <BRANCH_NAME>
```

**We have switched to a new branch. Now you can make changes to files, add new ones or do whatever you wish.**

### Step 4: Creating a pull request

After you satisfied with your changes, you can do pull request to the repo.

Add, commit and push your changes by doing:

```
# Adds all the modified files
git add .

# Commit the changes
git commit -m "Commit head message" -m "commit description"

# Push to origin to create a PR
git push origin <BRANCH_NAME>
```

Now go to your forked repo on github, and pull a request

<p align="center"><img src="https://raw.githubusercontent.com/theradcolor/Tweaks/master/assets/cont_img_pr.png"></p>

Have a good tittle and description

<p align="center"><img src="https://raw.githubusercontent.com/theradcolor/Tweaks/master/assets/cont_img_opn_pr.png"></p>

Adding a reviewer and labels is a good practice!

**Congratulations! You have successfully contributed to the project!**

## Localisation

Contribute to the app by adding different languages strings on it!

the string resource values can be found under `/app/src/main/res/values/strings.xml`

Further documentations can be found on [Android developers site](https://developer.android.com/training/basics/supporting-devices/languages).

**We are looking forward to your contributions!**
