'use strict';

describe('Controller Tests', function() {

    describe('UserPollVote Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockUserPollVote, MockUserProfile, MockPredictionResponse;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockUserPollVote = jasmine.createSpy('MockUserPollVote');
            MockUserProfile = jasmine.createSpy('MockUserProfile');
            MockPredictionResponse = jasmine.createSpy('MockPredictionResponse');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'UserPollVote': MockUserPollVote,
                'UserProfile': MockUserProfile,
                'PredictionResponse': MockPredictionResponse
            };
            createController = function() {
                $injector.get('$controller')("UserPollVoteDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'predictimoApp:userPollVoteUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
