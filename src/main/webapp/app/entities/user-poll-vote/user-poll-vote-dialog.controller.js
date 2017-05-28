(function() {
    'use strict';

    angular
        .module('predictimoApp')
        .controller('UserPollVoteDialogController', UserPollVoteDialogController);

    UserPollVoteDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'UserPollVote', 'UserProfile', 'PredictionPoll'];

    function UserPollVoteDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, UserPollVote, UserProfile, PredictionPoll) {
        var vm = this;

        vm.userPollVote = entity;
        vm.clear = clear;
        vm.save = save;
        vm.userprofiles = UserProfile.query();
        vm.predictionpolls = PredictionPoll.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.userPollVote.id !== null) {
                UserPollVote.update(vm.userPollVote, onSaveSuccess, onSaveError);
            } else {
                UserPollVote.save(vm.userPollVote, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('predictimoApp:userPollVoteUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
