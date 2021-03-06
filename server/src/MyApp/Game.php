<?php

namespace MyApp;
use MyApp\Chat;
use MyApp\TaskCalendar;

class Game {
    const POINTSPLIT = ":";
    const DATASPLIT = ";";
    const TASKLIMITSPLIT = "-";
    const TASKPLIT = "/";
    const SPLIT = ",";

    protected $gameName, $teacher, $password, $tasks, $users, $chats, $tasksData, $timeStart;

    public function __construct($gameName, $teacher, $password, $tasks, $users) {
        $this->gameName = $gameName;
        $this->teacher = $teacher;
        $this->password = $password;
        $this->tasks = array();
        $this->users = array();
        $this->chats = new \SplObjectStorage;
        $this->tasksData = new \SplObjectStorage;

        $taksSplit = explode(Game::TASKPLIT, $tasks);
        $usersSplit = explode(Game::SPLIT, $users);

        $file = fopen("src/MyApp/Names.csv","r");
        $userNames= fgetcsv($file);
        fclose($file);
        shuffle($userNames);   
        foreach($usersSplit as $user) {
            $userName = array_pop($userNames);
            array_push($this->users, array("name"=>$user, "userName"=>$userName, "id"=>"NoID"));
        }

        foreach($taksSplit as $task) {
            $taskArray = explode(Game::TASKLIMITSPLIT, $task);
            $name = $taskArray[0];
            $limit = $taskArray[1];
            $what = "";
            if(count($taskArray) > 2) {
                $what = $taskArray[2];
            }
            $where = "";
            if(count($taskArray) > 3) {
                $where = $taskArray[3];
            }

            array_push($this->tasks, array("name"=>$name, "limit"=>$limit, "what"=>$this->stringToArrayField($what), "where"=>$this->stringToArrayField($where)));

            $taskData = new TaskCalendar($name, $limit, $this->users, $this->stringToArrayField($what), $this->stringToArrayField($where));
            $this->tasksData->attach($taskData);
        }

        $this->timeStart = date("H:i:s");
    }

    private function stringToArrayField($string) {
        $array = explode(TaskCalendar::SPLIT, $string);
        if($array[0] !== "") {
            return $array;
        }
        else return array();
    }

    public function isUserInGame($userName) {
        foreach($this->users as $user) {
            if ($user == $userName) {
                return true;
            }
        }
        return false;
    }
    public function enterInGame($name, $id) {
        foreach($this->users as &$user) {
            if($user["name"] == $name) {
                $user["id"] = $id;
                return true;
            }
        }
        return false;
    }

    public function getTaskNames() {
        $tasks = Array();
        foreach($this->tasks as $task) {
            array_push($tasks, $task["name"]);
        }
        return $tasks;
    }
    public function getTaskLimit($taskName) {
        foreach ($this->tasks as $task) {
            if($task["name"] == $taskName) {
                return $task["limit"];
            }
        }
        return "NoTask";
    }
    public function getStringTasks() {
        $tasksString = "";
        foreach ($this->tasks as $task) {
            $tasksString = $tasksString . $task["name"] . Game::TASKLIMITSPLIT . $task["limit"] . Game::TASKLIMITSPLIT . $this->arrayToString($task["what"]) . Game::TASKLIMITSPLIT . $this->arrayToString($task["where"]) . Game::TASKPLIT;
        }
        $tasksString = rtrim($tasksString, Game::TASKPLIT);
        return $tasksString;
    }
    private function arrayToString($array) {
        $string = "";
        foreach ($array as $newString) {
            $string = $string . $newString . TaskCalendar::SPLIT;
        }
        $string = rtrim($string, TaskCalendar::SPLIT);
        return $string;
    }

    public function getUserNames() {
        return $this->getUserFields("name");
    }
    public function getUserUserNames() {
        return $this->getUserFields("userName");
    }
    public function getUserUserName($userName) {
        return $this->getUserField($userName, "name", "userName");
    }
    public function getUserName($userUserName) {
        return $this->getUserField($userUserName, "userName", "name");
    }
    public function getUserID($userName) {
        $id = $this->getUserField($userName, "name", "id");
        if($id == "NotFound") return "NoID";
        else return $id;
    }
    public function getUserID2($userUserName) {
        $id = $this->getUserField($userUserName, "userName", "id");
        if($id == "NotFound") return "NoID";
        else return $id;
    }
    public function getStringUsers() {
        return $this->getStringUserFields("name");
    }
    public function getStringUserNames() {
        return $this->getStringUserFields("userName");
    }
    public function getStringUserBothNames() {
        $usersString = "";
        foreach ($this->users as $user) {
            $usersString = $usersString . $user["name"] . " - " . $user["userName"] . Game::SPLIT;
        }
        $usersString = rtrim($usersString, ",");
        return $usersString;
    }
    private function getUserField($userField, $field, $fieldReturn) {
        foreach($this->users as $user) {
            if($user[$field] == $userField) {
                return $user[$fieldReturn];
            }
        }
        return "NotFound";
    }
    private function getUserFields($field) {
        $users = Array();
        foreach($this->users as $user) {
            array_push($users, $user[$field]);
        }
        return $users;
    }
    private function getStringUserFields($field) {
        $usersString = "";
        foreach ($this->users as $user) {
            $usersString = $usersString . $user[$field] . Game::SPLIT;
        }
        $usersString = rtrim($usersString, ",");
        return $usersString;
    }

    public function addMessage($userSender, $userDestination, $message) {
        foreach ($this->chats as $chat) {
            if($chat->isUsersInChat($userSender, $userDestination)) {
                $chat->addMessage($userDestination, $message);
                return true;
            }
        }
        $chat = new Chat($userSender, $userDestination, $this->getUserName($userSender), $this->getUserName($userDestination));
        $chat->addMessage($userDestination, $message);
        $this->chats->attach($chat);
        return false;
    }
    public function getChatFromUsers($userOne, $userTwo, $messagesSize) {
        foreach($this->chats as $chat) {
            if($chat->isUsersInChat($userOne, $userTwo) && ($messagesSize < $chat->getMessagesSize())) {
                return $chat->getMessagesUser($userTwo);
            }
        }
        return "";
    }

    public function addTaskCalendar($description, $userUserName, $location, $position, $partners, $what, $where) {
        foreach ($this->tasksData as $taskData) {
            if($taskData->getName() == $description) {
                $taskData->addUserData($userUserName, $this->getUserName($userUserName), $location, $position, $partners, $what, $where);
                return true;
            }
        }
        return false;
    }
    public function getTasksDataFromUser($userName) {
        $tasks = "";
        foreach($this->tasksData as $task) {
            $taskUser = $task->getUserTaskDataByRealName($userName);
            if($taskUser !== "NotFound") {
                $tasks = $tasks . $task->getName() . Game::DATASPLIT . $taskUser["location"] . Game::DATASPLIT . $taskUser["position"]["x"] . Game::SPLIT . $taskUser["position"]["y"] . Game::DATASPLIT . $task->partnersToString($taskUser["partners"]) . Game::DATASPLIT . $taskUser["what"] . Game::DATASPLIT. $taskUser["where"] . Game::POINTSPLIT;
            }
        }
        return $tasks;
    }
    public function validateTasksDataFromUser($userUserName) {
        $wrongTasks = "";
        foreach($this->tasksData as $task) {
            $taskUser = $task->getUserTaskDataByUserName($userUserName);
            if($taskUser !== "NotFound" && !$this->validate($task, $taskUser)) {
                $wrongTasks = $wrongTasks . $task->getName() . Game::POINTSPLIT;
            }
        }
        return $wrongTasks;
    }
    private function validate($task, $taskUser) {
        if(!empty($taskUser["partners"])) {
            foreach($taskUser["partners"] as $partner) {
                $taskPartner = $task->getUserTaskDataByUserName($partner);
                if($taskPartner == "NotFound" || ($taskUser["position"]["x"] !== $taskPartner["position"]["x"] || $taskUser["position"]["y"] !== $taskPartner["position"]["y"] || $taskUser["what"] !== $taskPartner["what"] || $taskUser["where"] !== $taskPartner["where"])) {
                    return false;
                }
            }
        }
        return true;
    }

    public function pickUpData() {
        return $this->pickUpChatData() . "\n\n" . $this->pickUpCalendarData();
    }
    private function pickUpChatData() {
        $chatsConversation = "CHATS \n================================================= \n\n";
        foreach($this->chats as $chat) {
            $chatsConversation = $chatsConversation . "------------ " . $chat->getUserRealName1() . " and " . $chat->getUserRealName2() . " ------------ \n";
            $chatsConversation = $chatsConversation . $chat->pickUpChat() . "\n";
        }
        return $chatsConversation;
    }
    private function pickUpCalendarData() {
        $calendarData = "TASKS \n================================================= \n\n";
        foreach($this->tasksData as $taskData) {
            $calendarData = $calendarData . "------------ " . $taskData->getName() . " ------------ \n";
            $calendarData = $calendarData . $taskData->pickUpTaskCalendar() . "\n";
        }
        return $calendarData;
    }

    public function getGameName() {
        return $this->gameName;
    }
    public function getTeacher() {
        return $this->teacher;
    }
    public function getPassword() {
        return $this->password;
    }
    public function getTasks() {
        return $this->tasks;
    }
    public function getUsers() {
        return $this->users;
    }
    public function getChats() {
        return $this->chats;
    }
    public function getTasksData() {
        return $this->tasksData;
    }
    public function getTimeStart() {
        return $this->timeStart;
    }
}

?>