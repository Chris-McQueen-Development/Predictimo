(function() {
    'use strict';

    angular
        .module('predictimoApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('user-poll-vote', {
            parent: 'entity',
            url: '/user-poll-vote',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'UserPollVotes'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/user-poll-vote/user-poll-votes.html',
                    controller: 'UserPollVoteController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('user-poll-vote-detail', {
            parent: 'user-poll-vote',
            url: '/user-poll-vote/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'UserPollVote'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/user-poll-vote/user-poll-vote-detail.html',
                    controller: 'UserPollVoteDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'UserPollVote', function($stateParams, UserPollVote) {
                    return UserPollVote.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'user-poll-vote',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('user-poll-vote-detail.edit', {
            parent: 'user-poll-vote-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-poll-vote/user-poll-vote-dialog.html',
                    controller: 'UserPollVoteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['UserPollVote', function(UserPollVote) {
                            return UserPollVote.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('user-poll-vote.new', {
            parent: 'user-poll-vote',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-poll-vote/user-poll-vote-dialog.html',
                    controller: 'UserPollVoteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                isCorrectVote: false,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('user-poll-vote', null, { reload: 'user-poll-vote' });
                }, function() {
                    $state.go('user-poll-vote');
                });
            }]
        })
        .state('user-poll-vote.edit', {
            parent: 'user-poll-vote',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-poll-vote/user-poll-vote-dialog.html',
                    controller: 'UserPollVoteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['UserPollVote', function(UserPollVote) {
                            return UserPollVote.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('user-poll-vote', null, { reload: 'user-poll-vote' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('user-poll-vote.delete', {
            parent: 'user-poll-vote',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-poll-vote/user-poll-vote-delete-dialog.html',
                    controller: 'UserPollVoteDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['UserPollVote', function(UserPollVote) {
                            return UserPollVote.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('user-poll-vote', null, { reload: 'user-poll-vote' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
