/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.api.rest;

import gov.epa.cef.web.service.UserService;
import gov.epa.cef.web.service.dto.TokenDto;
import gov.epa.cef.web.service.dto.UserDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserApiTest extends BaseApiTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserApi userApi;


    @Test
    public void retrieveCurrentUser_Should_ReturnUserDtoObject_WithStatusOk_WhenValidAuthenticatedUserExist() {
        UserDto userDto=new UserDto();
        when(userService.getCurrentUser()).thenReturn(userDto);
        ResponseEntity<UserDto> result=userApi.retrieveCurrentUser();
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(userDto, result.getBody());
    }

    @Test
    public void createToken_Should_ReturnNewGeneratedTokenWithStatusOk_WhenValidAuthenticatedUserExist() {
        TokenDto tokenDto=new TokenDto();
        when(userService.createToken()).thenReturn(tokenDto);
        ResponseEntity<TokenDto> result=userApi.createToken();
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(tokenDto, result.getBody());
    }

}
