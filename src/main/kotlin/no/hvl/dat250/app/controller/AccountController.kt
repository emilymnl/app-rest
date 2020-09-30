package no.hvl.dat250.app.controller

import no.hvl.dat250.app.API_VERSION_1
import no.hvl.dat250.app.dto.*
import no.hvl.dat250.app.exception.AccountNotFoundException
import no.hvl.dat250.app.exception.NotUniqueAccountEmailException
import no.hvl.dat250.app.repository.AccountRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.validation.Valid


/**
 * @author Elg
 */
@RestController
class AccountController {

    @Autowired
    lateinit var accountRepository: AccountRepository

    @GetMapping("$API_VERSION_1/account/{id}")
    fun publicAccountInformation(@PathVariable id: Long): PublicAccountResponse? {
        val account = accountRepository.findById(id)
        return if (account.isEmpty) {
            throw AccountNotFoundException(id)
        } else {
            account.get().toPublicResponse()
        }
    }

    @PostMapping("$API_VERSION_1/account")
    fun createAccount(@Valid @RequestBody accountRequest: AccountRequest): AccountResponse {
        if (accountRepository.existsAccountByEmail(accountRequest.email)) {
            throw NotUniqueAccountEmailException()
        }
        return accountRepository.saveAndFlush(accountRequest.toAccount()).toResponse()
    }
}
